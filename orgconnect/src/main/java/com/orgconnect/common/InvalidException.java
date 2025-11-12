package com.orgconnect.common;

import com.orgconnect.enums.ErrorCode;

//NS00072
public class InvalidException extends RuntimeException {
    ErrorCode errorCode;

    public InvalidException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
