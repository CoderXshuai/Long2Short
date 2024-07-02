package org.example.Long2Short.admin.common.convention.errorcode;

/**
 * @ClassName IErrorCode
 * @Description 添加描述
 * @Author CoderXshuai
 * @CreateTime 2024/6/30 23:30
 * @Version v1.0
 */

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
