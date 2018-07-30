package com.henrique.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(value = {"httpStatus", "apiMessage", "headers"})
public class HttpDTO {

    public Integer httpStatus;

    public String apiMessage;

    private Map<String, String> headers;

    public boolean hasHeaders() {
        if(headers != null && !headers.isEmpty()) return true;
        return false;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    protected void addHeadder(String headerName, String headerValue) {
        if(headers == null) headers = new HashMap<>();
        headers.put(headerName, headerValue);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpDTO [");
        sb.append("httpStatus = ").append(httpStatus);
        sb.append(", apiMessage = ").append(apiMessage);
        if(headers != null && !headers.isEmpty()) {
            sb.append(", headers = ").append(headers);
        }
        sb.append(']');
        return sb.toString();
    }
}
