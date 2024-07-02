# Long2Short

长链接转化为短连接

## 用户模块（admin）

主要用来管理用户

### 功能分析

- 检查用户名是否存在（前置流程，区分用户唯一标识）
- 注册用户
- 修改用户
- 根据用户名查询用户
- 用户登录
- 检查用户登录状态（是否）
- 用户退出登录
- 注销用户

### 全局配置

#### 全局返回实体对象

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

#### 全局异常码设计

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

#### 全局异常拦截器

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
