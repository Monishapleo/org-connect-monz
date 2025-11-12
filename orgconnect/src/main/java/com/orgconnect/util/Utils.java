package com.orgconnect.util;

import com.orgconnect.common.APIResponse;
import com.orgconnect.common.ErrorResponses;
import com.orgconnect.enums.ErrorCode;
import com.orgconnect.enums.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
public class Utils {
    private static final ZoneId ZONE_ID_IST = ZoneId.of("Asia/Kolkata");

    public static LocalDateTime getCurrentISTTime() {
        return LocalDateTime.now(ZONE_ID_IST).truncatedTo(ChronoUnit.SECONDS);
    }
    public static ResponseEntity<APIResponse> showSuccessObj(Object data, String message) {
        APIResponse response = new APIResponse();
        response.setCode(StatusCode.SUCCESS.code);
        response.setStatus(true);
        response.setMsg(message);
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<APIResponse> showError(String data, String message, int code, ErrorCode errorCode) {
        APIResponse errorResponse = new APIResponse();
        errorResponse.setStatus(false);
        errorResponse.setData(data);
        errorResponse.setMsg(message);
        errorResponse.setCode(code);

        if (errorCode != null) {
            ErrorResponses error = new ErrorResponses(errorCode);
            errorResponse.setError(error);
        }

        return ResponseEntity.status(code).body(errorResponse);
    }
}
