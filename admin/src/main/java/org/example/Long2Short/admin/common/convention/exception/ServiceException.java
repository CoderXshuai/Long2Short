package org.example.Long2Short.admin.common.convention.exception;

import org.example.Long2Short.admin.common.convention.errorcode.BaseErrorCode;
import org.example.Long2Short.admin.common.convention.errorcode.IErrorCode;

import java.util.Optional;

/**
 * @ClassName ServiceException
 * @Description 服务端异常
 * @Author CoderXshuai
 * @CreateTime 2024/6/30 23:49
 * @Version v1.0
 */

public class ServiceException extends AbstractException{
    public ServiceException(String message) {
        this(message, null, BaseErrorCode.SERVICE_ERROR);
    }

    public ServiceException(IErrorCode errorCode) {
        this(null, errorCode);
    }

    public ServiceException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(Optional.ofNullable(message).orElse(errorCode.message()), throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
