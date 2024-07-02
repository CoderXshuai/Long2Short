package org.example.Long2Short.admin.common.convention.exception;

import org.example.Long2Short.admin.common.convention.errorcode.BaseErrorCode;
import org.example.Long2Short.admin.common.convention.errorcode.IErrorCode;

/**
 * @ClassName RemoteException
 * @Description 远程调用异常
 * @Author CoderXshuai
 * @CreateTime 2024/6/30 23:49
 * @Version v1.0
 */

public class RemoteException extends AbstractException{
    public RemoteException(String message) {
        this(message, null, BaseErrorCode.REMOTE_ERROR);
    }

    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
