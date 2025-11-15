package com.user.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
//NS00072
public class APIResponse {
    private Integer code;
    private boolean status;
    private Object data;
    private Object error;
    private String msg;
    private Integer totalPages;
    private Long totalElements;
    private Integer pageNumber;
    private Integer pageData;
    private String response_data;


    public APIResponse() {
        this.code = code;
        this.status = status;
        this.data = data;
        this.error = error;
        this.msg = msg;

    }

    public APIResponse(int code, boolean status, String msg, Object data) {
        this.code = code;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //NS00072
    public APIResponse(int code, boolean status, String msg, Object data,
                       int totalPages, long totalElements, int pageData, int pageNumber) {
        this.code = code;
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.pageData = pageData;
        this.pageNumber = pageNumber;
    }

    public String getResponse_data() {
        return response_data;
    }

    public void setResponse_data(String response_data) {
        this.response_data = response_data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageData() {
        return pageData;
    }

    public void setPageData(Integer pageData) {
        this.pageData = pageData;
    }

}
