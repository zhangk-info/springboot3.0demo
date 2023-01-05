package com.xlj.common.exception;

import cn.hutool.core.exceptions.ValidateException;
import com.xlj.common.entity.DataResp;
import com.xlj.common.utils.MessageUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.security.sasl.AuthenticationException;
import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * 全局异常统一处理基础异常类
 */
@Slf4j
@ControllerAdvice
public class BaseHandlerForException {

    /**
     * ValidateException DTO验证异常
     *
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object validateExceptionHandler(HttpServletResponse response, ValidateException ex) {
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.SQL_ERROR.code, ErrorCode.SQL_ERROR.getMessage(ex.getMessage()), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * BadSqlGrammarException
     *
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object badSqlGrammarExceptionHandler(HttpServletResponse response, Exception ex) {
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.SQL_ERROR.code, ErrorCode.SQL_ERROR.getMessage(ex.getMessage()), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * SQLException
     *
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object sqlExceptionHandler(HttpServletResponse response, SQLException ex) {
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.SQL_ERROR.code, ex.getMessage(), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * ServiceException
     *
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object serviceExceptionHandler(HttpServletResponse response, ServiceException ex) {
        String i18nMsg;
        try {
            i18nMsg = MessageUtils.message(ex.getMessage());
        }catch (Exception e){
            i18nMsg = ex.getMessage();
        }
        DataResp<Object> dataResp = new DataResp<>(ex.getCode(), ex.getMessage(), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * FileNotFoundException
     *
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object fileNotFoundExceptionHandler(HttpServletResponse response, FileNotFoundException ex) {
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.DEFAULT.code, ex.getMessage(), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * Exception
     *
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Object otherExceptionHandler(HttpServletResponse response, Exception ex) {
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.DEFAULT.code, ex.getMessage(), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * AuthenticationException
     *
     * @param response
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object authenticationExceptionHandler(HttpServletResponse response, Exception ex) {
        DataResp<Object> dataResp = new DataResp<>(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }


}
