package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyUtils;
import cn.cimoc.broky.core.BrokyError;
import cn.cimoc.broky.core.BrokyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author LGZ
 * <p>
 */
@Slf4j
public class DefaultHttpMessageNotReadableExceptionHandler extends BaseHttpMessageNotReadableExceptionHandler {
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public BrokyResult handle(HttpMessageNotReadableException e) {
        log.warn(e.getMessage());
        return BrokyUtils.ajaxReturn(BrokyError.PARAM_ERROR.getErrCode(), BrokyError.PARAM_ERROR.getErrMsg());
    }
}
