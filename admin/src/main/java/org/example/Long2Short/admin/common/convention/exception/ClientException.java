package org.example.Long2Short.admin.common.convention.exception;

import org.example.Long2Short.admin.common.convention.errorcode.BaseErrorCode;
import org.example.Long2Short.admin.common.convention.errorcode.IErrorCode;

/**
 * @ClassName ClientException
 * @Description 客户端异常
 * @Author CoderXshuai
 * @CreateTime 2024/6/30 23:49
 * @Version v1.0
 */

public class ClientException extends AbstractException {

    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ClientException(String message) {
        this(message, null, BaseErrorCode.CLIENT_ERROR);
    }

    public ClientException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ClientException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}

