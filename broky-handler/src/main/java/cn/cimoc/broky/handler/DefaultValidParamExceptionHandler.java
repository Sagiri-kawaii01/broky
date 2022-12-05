package cn.cimoc.broky.handler;

import cn.cimoc.broky.core.BrokyUtils;
import cn.cimoc.broky.core.BrokyError;
import cn.cimoc.broky.core.BrokyResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LGZ
 * javax.validation参数验证的异常捕获
 */
@Slf4j
public class DefaultValidParamExceptionHandler extends BaseValidParamExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public BrokyResult handle(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        Map<String, String> error = new HashMap<>();
        fieldErrors.forEach((ex)-> error.put(ex.getField(), ex.getDefaultMessage()));
        return BrokyUtils.ajaxReturn(BrokyError.PARAM_VALID_ERROR.getErrCode(), BrokyError.PARAM_VALID_ERROR.getErrMsg(), error);
    }

}