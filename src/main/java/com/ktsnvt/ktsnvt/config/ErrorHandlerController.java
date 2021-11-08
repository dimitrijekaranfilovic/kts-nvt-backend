package com.ktsnvt.ktsnvt.config;


import com.ktsnvt.ktsnvt.valueobjects.ErrorInfo;
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
    ErrorInfo
    handleException(HttpServletRequest request, Exception ex) {
        return new ErrorInfo(request.getRequestURI(), ex.getMessage(), LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
