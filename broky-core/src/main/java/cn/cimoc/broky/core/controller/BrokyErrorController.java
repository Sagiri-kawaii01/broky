package cn.cimoc.broky.core.controller;

import cn.cimoc.broky.core.BrokyException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author LGZ
 *
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BrokyErrorController extends BasicErrorController {
    public BrokyErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @RequestMapping
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            throw new BrokyException(status.value(), status.getReasonPhrase());
        }
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        String path = "请求路径:" + body.get("path");
        String error = "错误信息:" + body.get("error");
        throw new BrokyException(status.value(), String.join(", ", path, error));
    }
}
