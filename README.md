# Long2Short

长链接转化为短连接，更高效解决方案可参考[京东短网址高可用提升最佳实践](https://juejin.cn/post/7382344353068974115)

## 后管系统（admin）

### 用户模块

主要用来管理用户

#### 功能分析

- 检查用户名是否存在（前置流程，区分用户唯一标识）
- 注册用户
- 修改用户
- 根据用户名查询用户
- 用户登录
- 检查用户登录状态（是否）
- 用户退出登录
- 注销用户

#### 全局配置

##### 全局返回实体对象

```java
  public class Result<T>{
    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 请求ID
     */
    private String requestId;
}
```

##### 全局异常码设计

1. 异常码说明
   根据阿里巴巴开发手册泰山版错误码为字符串类型，共 5 位，分成两个部分：错误产生来源+四位数字编号。说明：错误产生来源分为 A/B/C,四位数字编号从 0001 到 9999，大类之间的步长间距预留 100。。

- **A 表示错误来源于用户**，比如参数错误，用户安装版本过低，用户支付超时等问题；
- **B 表示错误来源于当前系统**，往往是业务逻辑出错，或程序健壮性差等问题；
- **C 表示错误来源于第三方服务**，比如 CDN 服务出错，消息投递超时等问题。
  异常码分类：一级宏观错误码、二级宏观错误码、三级详细错误码。
  客户端异常：


  | 错误码 | 中文描述           | 说明           |
  | ------ | ------------------ | -------------- |
  | A0001  | 用户端错误         | 一级宏观错误码 |
  | A0100  | 用户注册错误       | 二级宏观错误码 |
  | A0101  | 用户未同意隐私协议 |                |
  | A0102  | 注册国家或地区受限 |                |
  | A0110  | 用户名校验失败     |                |
  | A0111  | 用户名已存在       |                |
  | A0112  | 用户名包含敏感词   |                |
  | A0200  | 用户登录异常       | 二级宏观错误码 |
  | A02101 | 用户账户不存在     |                |
  | A02102 | 用户密码错误       |                |
  | A02103 | 用户账户已作废     |                |

  服务端异常：


  | 错误码 | 中文描述           | 说明           |
  | ------ | ------------------ | -------------- |
  | B0001  | 系统执行出错       | 一级宏观错误码 |
  | B0100  | 系统执行超时       | 二级宏观错误码 |
  | B0101  | 系统订单处理超时   |                |
  | B0200  | 系统容灾功能被触发 | 二级宏观错误码 |
  | B0210  | 系统限流           |                |
  | B0220  | 系统功能降级       |                |
  | B0300  | 系统资源异常       | 二级宏观错误码 |
  | B0310  | 系统资源耗尽       |                |
  | B0311  | 系统磁盘空间耗尽   |                |
  | B0312  | 系统内存耗尽       |                |

  远程调用异常：


  | 错误码 | 中文描述           | 说明           |
  | ------ | ------------------ | -------------- |
  | C0001  | 调用第三方服务出错 | 一级宏观错误码 |
  | C0100  | 中间件服务出错     | 二级宏观错误码 |
  | C0110  | RPC服务出错        |                |
  | C0111  | RPC服务未找到      |                |
  | C0112  | RPC服务未注册      |                |

2. 异常码设计

```java
public interface IErrorCode {
    /**
     * 错误码
     * @return
     */
     String code();
     /**
      * 错误信息
      * @return
      */
     String message();
}

```

##### 全局异常拦截器

![项目异常体系.png](assets/项目异常体系.png)

```java
public abstract class AbstractException extends RuntimeException {

    public final String errorCode;

    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = Optional.ofNullable(StringUtils.hasLength(message) ? message : null).orElse(errorCode.message());
    }
}

```

### 用户注册

#### 检查用户名是否存在

流程图
![检查用户名是否存在流程图.png](assets/检查用户名是否存在流程图.png)
存在的问题：
海量用户查询时，全部请求数据库，会将数据库直接打满。
解决方案：

1. 用户名加载缓存
   将所有用户名加载到Redis缓存中
   流程图
   ![用户名全部加载到缓存的方案.png](assets/用户名全部加载到缓存的方案.png)
   该方案存在的问题：

- 由于全部加载，只能设置永久数据
- 永久数据的Redis内存占用过高

2. 布隆过滤器
   在Redis缓存中引入布隆过滤器判断
   流程图：
   ![加了布隆过滤器的流程图](./assets/加了布隆过滤器的流程图.png)
   布隆过滤器是一种数据结构，用于快速判断一个元素是否存在于一个集合中。具体来说，布隆过滤器包含一个位数组和一组哈希函数。位数组的初始值全部置为
   0。在插入一个元素时，将该元素经过多个哈希函数映射到位数组上的多个位置，并将这些位置的值置为 1。
   ![布隆过滤器结构](./assets/布隆过滤器结构.png)
   在查询一个元素是否存在时，会将该元素经过多个哈希函数映射到位数组上的多个位置，如果所有位置的值都为
   1，则认为元素存在；如果存在任一位置的值为 0，则认为元素不存在。
   优点：

- 高效判别元素是否存在大规模集合中
- 节省内存
  缺点：
- 可能存在一定的误判率（不存在判断为存在）
  因而要对布隆过滤器设置合理的初始容量。初始容量越大，冲突几率越低。可以设置预期的误判率。
  在判断用户名是否存在的场景中，倘若将不存在的用户名判断为存在，例如123，对用户来说，可以修改为1234继续尝试，修改成本很低。
  如图为布隆过滤器执行流程图
  ![布隆过滤器执行流程图.png](assets/布隆过滤器执行流程图.png)

3. 代码中引入布隆过滤器

```java
//核心有两个参数，expectedInsertions和fasleProbability
boolean tryInit(long var1,double var3);
```

tryInit 有两个核心参数：

- expectedInsertions：预估布隆过滤器存储的元素长度。
- falseProbability：运行的误判率。

错误率越低，位数组越长，布隆过滤器的内存占用越大。
错误率越低，散列 Hash 函数越多，计算耗时较长。
因此使用布隆过滤器的场景

- 初始使用：注册用户时就向容器中新增数据，就不需要任务向容器存储数据了。
- 使用过程中引入：读取数据源将目标数据刷到布隆过滤器。

#### 用户注册功能

偶然发现，MyBatis-Plus从3.3.0版本开始，默认的ID生成器使用雪花算法结合不含中划线的UUID作为ID生成方式。这样的方式在分布式系统中生成的ID是唯一的，且是递增的，方便数据库索引。
用户注册流程图：
![用户注册流程图.png](assets/用户注册流程图.png)

1. 如何防止用户名重复？
   通过布隆过滤器把所有用户名进行加载，这样功能就能完全隔离数据库，并且在数据库层进行兜底，添加用户名唯一的索引。
2. 如何防止恶意请求大量注册同一个未注册的用户名？
   因为该用户名未注册，所以布隆过滤器不存在，代表可以插入数据库。但是恶意请求同一个用户名，这些请求都会落到数据库上，导致数据库压力过大。通过分布式锁，锁定该用户名进行串行执行，这样就能保证数据库不会受到恶意请求的影响。
   ![加分布式锁用户注册流程图.png](assets/加分布式锁用户注册流程图.png)
3. 如果恶意请求全部使用未注册用户名发起请求
   这样暂时无法避免，只能通过限流的方式，限制每个用户的注册次数，或者通过验证码的方式，增加注册的成本。

### 用户分库分表

#### 为什么要分库分表

- 数据量庞大
- 查询效率变慢
- 数据库连接不够

#### 什么是分库分表

分库和分表有两种模式，垂直和水平。

分库：

- 垂直分库：按照数据类型划分，比如电商数据库拆分为用户，订单，商品和交易数据库
  ![垂直分库.png](./assets/垂直分库.png)
- 水平分库：拆分为多个同类型的表，比如用户数据库拆分为User_DB_0-x
  ![水平分库](./assets/水平分库.png)

分表：

- 垂直分表：将数据库表按照业务维度进行拆分，不常用的信息放在一个扩展表中
  ![垂直分表](./assets/垂直分表.png)
- 水平分表：将用户表进行水平拆分，比如将用户表拆分为User_Table_0 - x
  ![水平分表](./assets/水平分表.png)

#### 什么场景下分库分表

1. 什么情况分表
   数据量过大或数据库对应的表占用的磁盘文件过大。
   具体取决于字段的数量和字段的的长度和字段的类型（特别是涉及text），一般垂直分表的情况下，主表不允许有text类型的字段，要放到拓展表中。
   一般来说，一个表的数据量超过1000w条，就可以考虑分表了，但如果字段简单，数据量小，可以适当放宽到3000w。
2. 什么情况下分库
   连接不够用（在QPS和TPS过高的情况下），单库的压力过大，可以考虑分库。
   MySQL Server 假设支持 4000 个数据库连接。一个服务连接池最大 10 个，假设有 40 个节点。已经占用了 400 个数据库连接。
   类似于这种服务，有10个，那你这个 MySQL Server 连接就不够了。
   但这种情况下，也不一定只能分库，一般还可以通过读写分离的形式来解决。只有在主从同步存在一定的延迟，不能满足要求很高的业务场景下，才需要考虑分库。
3. 什么情况下分库又分表

- 高并发写入或查询场景。
- 数据量巨大场景。

#### 数据库分库分表框架ShardingSphere

##### 分片键

用于将数据库（表）水平拆分的关键决策，分片键的选择直接影响到分片策略的选择。

分片键的选择原则：

1. 访问频率：选择访问频率高的字段作为分片键，将经常访问的数据放在同一分片，这样可以提高性能和减少跨分片查询的次数。
2. 数据均匀性：选择分片键时，要保证数据的均匀分布，避免数据倾斜，导致热点数据集中在某个分片上，影响性能。
3. 数据不可变性：分片键的值在数据生命周期内不可变更，不随着业务的变化而频繁修改。

对于Long2Short项目，选择用户名作为分片键，因为用户名是唯一的，且访问频率高，数据均匀分布，不可变更。
不采用用户ID作为分片键的原因：用户ID在登录时，不会传入，而用户名在登录时会传入。
而SQL语句如果不传入分片键，会导致全表扫描，性能低下。
通过JDBC和Proxy两种方式实现分库分表。

##### 引入ShardingSphere-JDBC到项目

1. 引入依赖
2. 定义分片规则

```yaml
# 采用的是基于JDBC的分库分表，因此需要对数据源进行配置
spring:
  datasource:
    # ShardingSphere 对 Driver 自定义，实现分库分表等隐藏逻辑
    driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
    # ShardingSphere 配置文件路径
    url: jdbc:shardingsphere:classpath:shardingsphere-config.yaml
```

新建shardingsphere-config.yaml文件

```yaml
# 数据源集合
dataSources:
  ds_0:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://127.0.0.1:3306/long2short?useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&allowMultiQueries=true&serverTimezone=Asia/Shanghai
    username:
    password:

rules:
  - !SHARDING
    tables:
      t_user:
        # 真实数据节点，比如数据库源以及数据库在数据库中真实存在的
        actualDataNodes: ds_0.t_user_${0..15}
        # 分表策略
        tableStrategy:
          # 用于单分片键的标准分片场景
          standard:
            # 分片键
            shardingColumn: username
            # 分片算法，对应 rules[0].shardingAlgorithms
            shardingAlgorithmName: user_table_hash_mod
    # 分片算法
    shardingAlgorithms:
      # 数据表分片算法
      user_table_hash_mod:
        # 根据分片键 Hash 分片
        type: HASH_MOD
        # 分片数量
        props:
          sharding-count: 16
# 展现逻辑 SQL & 真实 SQL
props:
  sql-show: true
```

逻辑表：t_user，相同结构的水平拆分数据库的逻辑标识，对用户程序透明。
实际表：ds_0.t_user_${0..15}，真实存在的数据库表名。

### 敏感信息加密存储

主要通过shardingsphere来实现，在shardingsphere-config.yaml中配置加密规则

```yaml
- !ENCRYPT
  # 需要加密的表集合
  tables:
    # 用户表
    t_user:
      # 用户表中哪些字段需要进行加密
      columns:
        # 手机号字段，逻辑字段，不一定是在数据库中真实存在
        phone:
          # 手机号字段存储的密文字段，这个是数据库中真实存在的字段
          cipherColumn: phone
          # 身份证字段加密算法
          encryptorName: common_encryptor
        mail:
          cipherColumn: mail
          encryptorName: common_encryptor
      # 是否按照密文字段查询，主要在企业级开发测试环境下使用，比如前期测试既存储明文又存储密文，后期只存储密文
      queryWithCipherColumn: true
  # 加密算法
  encryptors:
    # 自定义加密算法名称
    common_encryptor:
      # 加密算法类型
      type: AES
      props:
        # AES 加密密钥
        aes-key-value: 
```

加密原理是将SQL语句改写，在JDBC层面对数据进行加密和解密，对用户透明。
![加密原理](./assets/加密原理.png) 