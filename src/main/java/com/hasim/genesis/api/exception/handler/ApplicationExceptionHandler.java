
package com.hasim.genesis.api.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hasim.genesis.api.exception.ApplicationDownstreamException;
import com.hasim.genesis.api.exception.ApplicationNotFoundException;
import com.hasim.genesis.api.exception.ApplicationSQLException;
import com.hasim.genesis.api.exception.ErrorCodes;
import com.hasim.genesis.api.exception.MaxValueLengthException;
import com.hasim.genesis.api.exception.MissingValueException;
import com.hasim.genesis.api.model.ErrorResponse;


/**
 * Advice to handle exception
 * 
 * @author hasmolla
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(basePackages = { " com.hasim.genesis.api.controller" })
public class ApplicationExceptionHandler {

    private static final Logger logger = LogManager.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleApplicationNotFoundException(
        ApplicationNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorCodes.APPLICATION_NOT_FOUND.getErrorResponse();
        logger.error(error, ex);

        return new ResponseEntity<>(
            error,
            HttpStatus.NOT_FOUND);
    }
    
    
    
    @ExceptionHandler(ApplicationDownstreamException.class)
    public ResponseEntity<ErrorResponse> handleApplicationDownstreamException(
    		ApplicationDownstreamException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorCodes.DOWNSTREAM_EXCEPTION.getErrorResponse();
        logger.error(error, ex);

        return new ResponseEntity<>(
            error,
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(MissingValueException.class)
    public ResponseEntity<ErrorResponse> handleMissingValueException(HttpServletRequest request,
        MissingValueException ex) {
        ErrorResponse error = new ErrorResponse(
            ErrorCodes.MANDATORY_FIELD.getCode(),
            ErrorCodes.missingOrEmptyHeaderErrorMsg(ex.getMessage()));
        logger.error(error, ex);
        return new ResponseEntity<ErrorResponse>(
            error,
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxValueLengthException.class)
    public ResponseEntity<ErrorResponse> handleMaxValueLengthException(HttpServletRequest request,
        MaxValueLengthException ex) {
        ErrorResponse error = new ErrorResponse(
            ErrorCodes.MANDATORY_FIELD.getCode(),
            ErrorCodes.invalidHeaderLength(ex.getValue(),ex.getMaxLength()));
        logger.error(error, ex);
        return new ResponseEntity<ErrorResponse>(
            error,
            HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ApplicationSQLException.class)
    public ResponseEntity<ErrorResponse> handleHealthBoardSQLException(HttpServletRequest request,
        ApplicationSQLException ex) {
        ErrorResponse error = new ErrorResponse(
            ErrorCodes.SQL_EXCEPTION.getCode(),
            ErrorCodes.SQL_EXCEPTION.getDescription());
        logger.error(error, ex);
        return new ResponseEntity<ErrorResponse>(
            error,
            HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request,
        Exception ex) {
        ErrorResponse error = new ErrorResponse(
            ErrorCodes.APPLICATION_EXCEPTION.getCode(),
            ErrorCodes.APPLICATION_EXCEPTION.getDescription());
        logger.error(error, ex);
        return new ResponseEntity<ErrorResponse>(
            error,
            HttpStatus.BAD_REQUEST);
    }
    
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
        HttpServletRequest request, HttpMessageNotReadableException ex) {
        ErrorResponse error = new ErrorResponse(
            ErrorCodes.MANDATORY_FIELD.getCode(),
            ErrorCodes.MANDATORY_FIELD.getDescription());
        logger.error(error, ex);
        return new ResponseEntity<ErrorResponse>(
            error,
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<ErrorResponse> handleServletRequestBindingException(
        HttpServletRequest request, ServletRequestBindingException ex) {

        ErrorResponse error = null;
        
        error = error == null ? new ErrorResponse(
            ErrorCodes.MANDATORY_FIELD.getCode(),
            ex.getMessage()) : error;
        logger.error(error, ex);
        return new ResponseEntity<>(
            error,
            HttpStatus.BAD_REQUEST);
    }
    
}
