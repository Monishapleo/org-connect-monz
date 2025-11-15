package com.user.enums;

public enum StatusCode {
    SUCCESS(200),
    BADREQUEST(400),
    FORBIDDEN(403);

    public int code;

    StatusCode(int code) {
        this.code = code;
    }
}

