package cn.cimoc.broky.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author LGZ
 * <p>
 */
@Slf4j
public class DefaultBrokyLogHandler implements BrokyLogHandler {

    private BrokyHandledExceptionPool pool;

    public DefaultBrokyLogHandler(BrokyHandledExceptionPool pool) {
        this.pool = pool;
    }

    @Override
    public void handler(JoinPoint jp, Object keys, Throwable e, BrokyLogHandlerConfig handlerConfig) {
        if (null != e) {
            if (pool.exist(e.getClass())) {
                handlerConfig.setExcFullShow(false);
                BrokyLogVO logVO = this.getLog(jp, keys, e, handlerConfig);
                logVO.setExecTime(handlerConfig.getEndAt() - handlerConfig.getStartAt());
                log.warn(logVO.toString());
                return;
            }
            BrokyLogVO logVO = this.getLog(jp, keys, e, handlerConfig);
            logVO.setExecTime(-1L);
            log.error(logVO.toString());
        } else {
            long time;
            if ((time = handlerConfig.getEndAt() - handlerConfig.getStartAt()) >= handlerConfig.getRunTime()) {
                BrokyLogVO logVO = this.getLog(jp, keys, null, handlerConfig);
                logVO.setExecTime(time);
                log.info(logVO.toString());
            }
        }
    }

    protected String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements, BrokyLogHandlerConfig handlerConfig) {
        StringBuilder builder = new StringBuilder();
        if(handlerConfig.getExcFullShow()){
            for (StackTraceElement stet : elements) {
                builder.append(stet).append("\n");
            }
            return exceptionName + ":" + exceptionMessage + "\n\t" + builder;
        }
        return exceptionName + ":" + exceptionMessage;
    }

    /**
     * 获取访问者的ip地址
     * 注：要外网访问才能获取到外网地址，如果你在局域网甚至本机上访问，获得的是内网或者本机的ip
     */
    protected static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            //X-Forwarded-For：Squid 服务代理
            String ipAddresses = request.getHeader("X-Forwarded-For");

            if (ipAddresses == null || ipAddresses.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddresses)) {
                //Proxy-Client-IP：apache 服务代理
                ipAddresses = request.getHeader("Proxy-Client-IP");
            }

            if (ipAddresses == null || ipAddresses.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddresses)) {
                //WL-Proxy-Client-IP：weblogic 服务代理
                ipAddresses = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ipAddresses == null || ipAddresses.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddresses)) {
                //HTTP_CLIENT_IP：有些代理服务器
                ipAddresses = request.getHeader("HTTP_CLIENT_IP");
            }

            if (ipAddresses == null || ipAddresses.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddresses)) {
                //X-Real-IP：nginx服务代理
                ipAddresses = request.getHeader("X-Real-IP");
            }

            //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
            if (ipAddresses != null && ipAddresses.length() != 0) {
                ipAddress = ipAddresses.split(",")[0];
            }

            //还是不能获取到，最后再通过request.getRemoteAddr();获取
            if (ipAddress == null || ipAddress.length() == 0 ||
                    "unknown".equalsIgnoreCase(ipAddresses)) {
                ipAddress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            ipAddress = "";
        }
        return ipAddress;
    }

    protected String getNowDate(){
        Date now = new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(now);
    }

    protected BrokyLogVO getLog(JoinPoint jp, Object keys, Throwable e, BrokyLogHandlerConfig handlerConfig) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 输出日志VO
        BrokyLogVO logVo = new BrokyLogVO();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) jp.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            BrokyLog opLog = method.getAnnotation(BrokyLog.class);
            if (opLog == null) {
                opLog = method.getDeclaringClass().getAnnotation(BrokyLog.class);
            }
            logVo.setModel(opLog.module());
            logVo.setOptType(opLog.optType());
            logVo.setDescription(opLog.description());
            // 获取请求的类名
            String className = jp.getTarget().getClass().getName();
            logVo.setClassName(className);
            // 获取请求的方法名
            String methodName = method.getName();
            logVo.setMethodName(methodName);

            //请求uri
            String uri = request.getRequestURI();
            logVo.setUri(uri);
            logVo.setIp(getIpAddr(request));
            //操作时间点
            logVo.setReqTime(getNowDate());

            //异常名称+异常信息
            if(null != e){
                logVo.setExcName(e.getClass().getName());
                logVo.setExcInfo(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace(), handlerConfig));
            }
            //请求的参数，参数所在的数组转换成json
            String params =  Arrays.toString(jp.getArgs());
            logVo.setParams(params);
            //返回值
            if(null != keys && Void.class.getName() != keys){
                StringBuilder result = new StringBuilder(handlerConfig.getObjectMapper().writeValueAsString(keys));
                if (handlerConfig.getResultLength() == 0){
                    //表示全部
                    logVo.setReturnValue(result.toString());
                } else {
                    String tempResult=result.substring(0, handlerConfig.getResultLength());
                    logVo.setReturnValue(tempResult);
                }
            }
        } catch (Exception ignored) {

        }
        return logVo;
    }
}
