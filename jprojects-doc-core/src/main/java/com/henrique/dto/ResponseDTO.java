package com.henrique.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.*;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ResponseDTO {

    private Integer httpStatus;
    private Object meta;
    private String message;

    public ResponseDTO() {
        super();
    }

    public ResponseDTO(Integer httpStatus, String message) {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ResponseDTO(Integer httpStatus, Object meta, String message) {
        super();
        this.httpStatus = httpStatus;
        if(!meta.getClass().isAssignableFrom(HttpDTO.class)) this.meta = meta;
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResponseDTO [httpStatus = ").append(httpStatus);
        sb.append(", meta = ").append(meta);
        sb.append(", message = ").append(message);
        sb.append("]");
        return sb.toString();
    }

}
