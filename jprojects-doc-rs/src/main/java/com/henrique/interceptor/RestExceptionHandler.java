package com.henrique.interceptor;

import com.henrique.dto.HttpDTO;
import com.henrique.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    // HTTP ERROR CODES 2*
    @ExceptionHandler(value = {NoResultException.class})
    protected ResponseEntity<Object> handleNoContent(Exception e, WebRequest request) {
        HttpDTO httpDTO = new HttpDTO();
        httpDTO.httpStatus = HttpStatus.NO_CONTENT.value();
        httpDTO.apiMessage = e.getMessage();

        return handleExceptionInternal(e, httpDTO, new HttpHeaders(), HttpStatus.NO_CONTENT, request);
    }

    // HTTP ERROR CODES 4*
    @ExceptionHandler(value = {FileAlreadyExistsException.class})
    protected ResponseEntity<Object> handleConflict(Exception e, WebRequest request) {
        HttpDTO httpDTO = new HttpDTO();
        httpDTO.httpStatus = HttpStatus.CONFLICT.value();
        httpDTO.apiMessage = e.getMessage();

        return handleExceptionInternal(e, httpDTO, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {Exception.class, RequestException.class})
    protected ResponseEntity<Object> handleBadRequest(Exception e, WebRequest request) {
        HttpDTO httpDTO = new HttpDTO();
        httpDTO.httpStatus = HttpStatus.BAD_REQUEST.value();
        httpDTO.apiMessage = e.getMessage();

        return handleExceptionInternal(e, httpDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {NoSuchScopeException.class, NoSuchModuleException.class})
    protected ResponseEntity<Object> handlePreConditionFailed(Exception e, WebRequest request) {
        HttpDTO httpDTO = new HttpDTO();
        httpDTO.httpStatus = HttpStatus.PRECONDITION_FAILED.value();
        httpDTO.apiMessage = e.getMessage();

        return handleExceptionInternal(e, httpDTO, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED, request);
    }

    // HTTP ERROR CODES 5*
    @ExceptionHandler(value = {FileManipulationException.class})
    protected ResponseEntity<Object> handleInternalServerError(Exception e, WebRequest request) {
        HttpDTO httpDTO = new HttpDTO();
        httpDTO.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        httpDTO.apiMessage = e.getMessage();

        return handleExceptionInternal(e, httpDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


}
