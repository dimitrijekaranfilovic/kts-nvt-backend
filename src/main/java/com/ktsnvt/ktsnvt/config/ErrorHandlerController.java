package com.ktsnvt.ktsnvt.config;


import com.ktsnvt.ktsnvt.exception.*;
import org.hibernate.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandlerController {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ErrorInfo handleBusinessException(HttpServletRequest request, BusinessException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public ErrorInfo handleBadCredentialsException(HttpServletRequest request, BadCredentialsException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ErrorInfo handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SuperUserNotFoundException.class)
    @ResponseBody
    public ErrorInfo handleSuperUserNotFoundException(HttpServletRequest request, SuperUserNotFoundException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND);
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(QueryException.class)
    @ResponseBody
    public ErrorInfo handleQueryException(HttpServletRequest request, QueryException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getCause().getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({OptimisticLockingFailureException.class, PessimisticLockingFailureException.class})
    @ResponseBody
    public ErrorInfo handleConcurrencyFailureException(HttpServletRequest request, ConcurrencyFailureException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getCause().getMessage(), LocalDateTime.now(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OrderItemGroupExistsException.class)
    @ResponseBody
    public ErrorInfo handleOrderItemGroupExistsException(HttpServletRequest request, OrderItemGroupExistsException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ErrorInfo handleNotReadableException(HttpServletRequest request, RuntimeException ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ErrorInfo handleMethodBindingException(HttpServletRequest request, BindException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        ex.getBindingResult().getGlobalErrors()
                .forEach(e -> errors.put(e.getObjectName(), e.getDefaultMessage()));

        return new ErrorInfo(request.getRequestURI(), ex.getLocalizedMessage(), LocalDateTime.now(),
                HttpStatus.BAD_REQUEST, errors);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ErrorInfo handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex){
        return new ErrorInfo(request.getRequestURI(), "You are not authorized to access this resource.", LocalDateTime.now(), HttpStatus.FORBIDDEN);
    }


}
