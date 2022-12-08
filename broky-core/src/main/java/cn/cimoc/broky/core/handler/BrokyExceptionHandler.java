package cn.cimoc.broky.core.handler;

import cn.cimoc.broky.core.BrokyException;
import cn.cimoc.broky.core.BrokyResult;
import cn.cimoc.broky.core.BrokyUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author LGZ
 *
 */
@ControllerAdvice
public class BrokyExceptionHandler {
    @ExceptionHandler(value = BrokyException.class)
    @ResponseBody
    public BrokyResult error(BrokyException e) {
        return BrokyUtils.ajaxReturn(e.getErrCode(), e.getMessage());
    }
}
