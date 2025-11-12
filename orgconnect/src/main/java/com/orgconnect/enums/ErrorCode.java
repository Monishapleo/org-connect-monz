package com.orgconnect.enums;

public enum ErrorCode {
    ORG_NAME_EMPTY("ER-1001", "Organisation name cannot be empty"),
    UNAUTHORIZED_ACCESS("ER-1002", "Unauthorized access "),
    NO_DATA_FOUND("ER-1003", "No data found for the given request");

    public final String code;
    public final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
