package com.orgconnect.enums;

public enum ErrorCode {
    REQUIRED_FIELDS_MISSING("ER-1001", "Required fields missing "),
    UNAUTHORIZED_ACCESS("ER-1002", "Unauthorized access "),
    NO_DATA_FOUND("ER-1003", "No data found for the given request"),
    ORG_NOT_FOUND("ER-1004", "Organisation not found"),
    MISSING_TOKEN("ER-1005", "Auth token missing"),
    INTERNAL_SERVER_ERROR("ER-1006", "Internal server error "),
    ORG_ID_MISSING("ER-1007", "ORG-ID missing");

    public final String code;
    public final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
