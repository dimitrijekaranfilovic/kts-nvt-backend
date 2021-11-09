package com.ktsnvt.ktsnvt.config;


import com.ktsnvt.ktsnvt.exception.BusinessException;
import com.ktsnvt.ktsnvt.exception.ErrorInfo;
import com.ktsnvt.ktsnvt.exception.NotFoundException;
import com.ktsnvt.ktsnvt.exception.OrderItemGroupExistsException;
import org.hibernate.QueryException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandlerController {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleException(HttpServletRequest request, Exception ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ErrorInfo handleBusinessException(HttpServletRequest request, BusinessException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(QueryException.class)
    @ResponseBody
    public ErrorInfo handleQueryException(HttpServletRequest request, QueryException ex){
        return new ErrorInfo(request.getRequestURI(), ex.getCause().getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OrderItemGroupExistsException.class)
    @ResponseBody
    public ErrorInfo handleOrderItemGroupExistsException(HttpServletRequest request, OrderItemGroupExistsException ex){
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.CONFLICT);
    }
}
