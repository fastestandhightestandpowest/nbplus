package cn.itcast.controller;

import cn.itcast.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @Author hua
 * @date 2019/07/20 11:31
 **/
@ControllerAdvice
public class HealthException{

    private Logger logger = LoggerFactory.getLogger(HealthException.class);

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public Result AccessDeniedException(AccessDeniedException e){
        logger.error("权限不足",e);
        return new Result(false,"权限不足");
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result ConstraintViolationException(MethodArgumentNotValidException e){
        logger.error("校验不合格",e);
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuffer stringBuffer = new StringBuffer();
        for (FieldError fieldError : fieldErrors) {
            stringBuffer.append(fieldError.getField()+":"+fieldError.getDefaultMessage());
            stringBuffer.append(", ");
        }
        //减去后面多余的", "
        if (stringBuffer.length()>0){
            stringBuffer.setLength(stringBuffer.length()-2);
        }
        String message = stringBuffer.toString();
        return new Result(false,message);
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public Result handlerException(RuntimeException e){
        logger.error("出错啦",e);
        return new Result(false,e.getMessage());
    }
}
