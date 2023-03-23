package com.xlj.common.exception;

import cn.hutool.core.exceptions.ValidateException;
import com.xlj.common.entity.DataResp;
import com.xlj.common.utils.MessageUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.security.sasl.AuthenticationException;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常统一处理基础异常类
 */
@Slf4j
@ControllerAdvice
public class BaseHandlerForException {

    /**
     * ValidateException DTO验证异常
     *
     * @param response response
     * @param ex       ex
     * @return Object
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object validateExceptionHandler(HttpServletResponse response, ValidateException ex) {
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.PARAMS_ERR.code, ErrorCode.PARAMS_ERR.getMessage(ex.getMessage()), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * ValidateException DTO验证异常
     *
     * @param response response
     * @param ex       ex
     * @return Object
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object methodArgumentNotValidExceptionHandler(HttpServletResponse response, MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        String message = allErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("."));
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.PARAMS_ERR.code, ErrorCode.PARAMS_ERR.getMessage(message), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * BadSqlGrammarException
     *
     * @param response response
     * @param ex       ex
     * @return Object
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object badSqlGrammarExceptionHandler(HttpServletResponse response, Exception ex) {
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.SQL_ERROR.code, ErrorCode.SQL_ERROR.getMessage(ex.getMessage()), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * SQLException
     *
     * @param response response
     * @param ex       ex
     * @return Object
     */
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object sqlExceptionHandler(HttpServletResponse response, SQLException ex) {
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.SQL_ERROR.code, ex.getMessage(), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * ServiceException
     *
     * @param response response
     * @param ex       ex
     * @return Object
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object serviceExceptionHandler(HttpServletResponse response, ServiceException ex) {
        String i18nMsg;
        try {
            i18nMsg = MessageUtils.message(ex.getMessage());
        } catch (Exception e) {
            i18nMsg = ex.getMessage();
        }
        i18nMsg = i18nMsg.substring(!i18nMsg.contains("::") ? 0 : i18nMsg.indexOf("::") + 2);
        DataResp<Object> dataResp = new DataResp<>(ex.getCode(), i18nMsg, null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }

    /**
     * FileNotFoundException
     *
     * @param response response
     * @param ex       ex
     * @return Object
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
     * @param response response
     * @param ex       ex
     * @return Object
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object otherExceptionHandler(HttpServletResponse response, Exception ex) {
        String msg = "";
        if (ex.getClass().getName().equals("org.springframework.security.access.AccessDeniedException")) {
            msg = "无权访问";
        }
        DataResp<Object> dataResp = new DataResp<>(ErrorCode.DEFAULT.code, StringUtils.isBlank(msg) ? ex.getMessage() : msg, null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }


    /**
     * AuthenticationException
     *
     * @param response response
     * @param ex       ex
     * @return Object
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object authenticationExceptionHandler(HttpServletResponse response, AuthenticationException ex) {
        DataResp<Object> dataResp = new DataResp<>(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), null);
        log.error(ex.getMessage(), ex);
        return dataResp;
    }


}
