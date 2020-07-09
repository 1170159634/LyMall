package com.leyou.common.common;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//手动异常类
@ControllerAdvice
public class commonExceptionHandler
{
    @ExceptionHandler(LyException.class)
    public ResponseEntity<ExceptionResult> handlerException(LyException e)
    {
        ExceptionEnums em=e.getExceptionEnums();
       return ResponseEntity.status(em.getCode())
               .body(new ExceptionResult(em));
    }
}
