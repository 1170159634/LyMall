package com.leyou.common.vo;

import com.leyou.common.enums.ExceptionEnums;
import lombok.Data;

@Data
public class ExceptionResult
{
    private int code;
    private String msg;
    private Long timestamp;

    public ExceptionResult(ExceptionEnums exceptionEnums)
    {
        this.code=exceptionEnums.getCode();
        this.msg=exceptionEnums.getMsg();
        this.timestamp=System.currentTimeMillis();
    }
}
