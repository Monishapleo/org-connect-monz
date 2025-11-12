/*
package com.orgconnect.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.npg.courierservice.enumclass.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@ControllerAdvice
//NS00072
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleGenericException(Exception e) {
        APIResponse response = new APIResponse();
        ErrorResponses errorResponse = new ErrorResponses(ErrorCode.INTERNAL_SERVER_ERROR);
        errorResponse.additionalInfo.excepCode = ErrorCode.INTERNAL_SERVER_ERROR.code;
        errorResponse.additionalInfo.excepText = e.getMessage();
        response.setError(errorResponse);
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMsg("something went wrong");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<APIResponse> handleRestClientResponseException(RestClientResponseException ex) {
        APIResponse response = new APIResponse();
        ErrorResponses errorResponse = new ErrorResponses(ErrorCode.UNABLE_TO_FETCH_TRACKING);

        try {
            Map<String, Object> body = objectMapper.readValue(ex.getResponseBodyAsString(), new TypeReference<>() {});
            errorResponse.additionalInfo.excepCode = ErrorCode.UNABLE_TO_FETCH_TRACKING.code;
            errorResponse.additionalInfo.excepText = body.toString();
        } catch (Exception e) {
            errorResponse.additionalInfo.excepCode = ErrorCode.UNABLE_TO_FETCH_TRACKING.code;
            errorResponse.additionalInfo.excepText = ex.getResponseBodyAsString();
        }

        response.setError(errorResponse);
        response.setCode(ex.getRawStatusCode());
        response.setMsg("Something went wrong");

        return ResponseEntity.status(ex.getRawStatusCode()).body(response);
    }

    // TODO: Update this switch case when adding new error codes.
    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<APIResponse> handleInvalidException(InvalidException e) {
        APIResponse response = new APIResponse();
        ErrorResponses errorResponse = new ErrorResponses(e.errorCode);

        errorResponse.additionalInfo.excepCode = e.errorCode.code;
        errorResponse.additionalInfo.excepText = e.getMessage();

        response.setError(errorResponse);
        response.setMsg(e.getMessage());

        HttpStatus status;

        switch (e.errorCode) {
            case MISSING_TOKEN:
                status = HttpStatus.FORBIDDEN;
                break;
            case UNAUTHORIZED_ACCESS:
                status = HttpStatus.UNAUTHORIZED;
                break;
            default:
                status = HttpStatus.BAD_REQUEST;
        }

        response.setCode(status.value());

        return ResponseEntity.status(status).body(response);
    }

}
*/
