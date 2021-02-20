package cn.nhmt.blog.controller;

import cn.nhmt.blog.contants.ErrorCode;
import cn.nhmt.blog.bo.exception.FileUploadException;
import cn.nhmt.blog.dto.Message;
import cn.nhmt.blog.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: global exception controller
 * @Date: 2020-04-03 17:33
 * @Author: PinJyu
 * @Version: 1.0
 **/
@Slf4j
@RestControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {

    @ResponseStatus
    @ExceptionHandler({Exception.class})
    Message globalException(Exception e) {
        if (e instanceof AccessDeniedException || e instanceof AuthenticationException) {
            throw WebUtil.unresolverException;
        }
        log.error("请求处理其期间发生未知异常：", e);
        Message message = new Message(false);
        message.setErrorCode(ErrorCode.SERVER_ERROR);
        message.setMessage("服务器发生未知错误，请稍后重试。");
        return message;
    }

    @ResponseStatus
    @ExceptionHandler({PersistenceException.class})
    Message persistenceException(PersistenceException e) {
        log.error("请求处理期间发生持久化异常：", e);
        Message message = new Message(false);
        message.setErrorCode(ErrorCode.PERSISTENCE_ERROR);
        message.setMessage("服务器持久层发生异常，请稍后重试。");
        return message;
    }

    @ResponseStatus
    @ExceptionHandler({FileUploadException.class})
    Message fileUploadException(FileUploadException e) {
        log.error("请求处理期间发生IO异常：", e);
        Message message = new Message(false);
        message.setErrorCode(ErrorCode.FileUpload_ERROR);
        message.setMessage("服务器处理上传文件发生异常。");
        return message;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    Message constraintVioilationException(ConstraintViolationException e) {
        log.error("Request paramters valid fail:", e);
        Message message = new Message(false);
        message.setErrorCode(ErrorCode.ARGUMENTS_UNLEGAL);
        message.setMessage("参数验证发生错误，参数不合法。");
        e.getConstraintViolations().forEach(p -> message.put(p.getPropertyPath().toString(), p.getMessage()));
        return message;
    }


    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Http exception:", ex);
        if (!WebUtil.isAjaxWebRequest(request)) {
            throw WebUtil.unresolverException;
        }

        Message message = new Message(false);
        if (ex instanceof BindException) {
            BindException bindException = (BindException) ex;
            BindingResult bindingResult = bindException.getBindingResult();
            message.setData(bindingResultMap(bindingResult));
        }

        return super.handleExceptionInternal(ex, message, headers, status, request);
    }

    private Map<String, Object> bindingResultMap(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream().collect(Collectors.toMap(e -> {
            FieldError fieldError = (FieldError) e;
            return fieldError.getObjectName() + "." + fieldError.getField();
        }, e -> {
            String message = e.getDefaultMessage();
            return message != null ? message : "message null";
        }));
    }

}
