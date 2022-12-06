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
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 输出日志VO
        BrokyLogVO logVO = new BrokyLogVO();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) jp.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            BrokyLog anno = method.getAnnotation(BrokyLog.class);
            if (anno == null) {
                anno = method.getDeclaringClass().getAnnotation(BrokyLog.class);
            }
            setRuntimeFromAnnotation(anno, method, handlerConfig);
            if (needLog(e, handlerConfig)) {
                return;
            }
            copyAnnotationValue(anno, logVO);
            // 获取请求的类名
            logVO.setClassName(jp.getTarget().getClass().getName());
            // 获取请求的方法名
            logVO.setMethodName(method.getName());

            //请求uri
            logVO.setUri(request.getRequestURI());
            // 请求ip
            logVO.setIp(getIpAddr(request));
            //操作时间点
            logVO.setReqTime(getNowDate());

            //异常名称+异常信息
            if(null != e){
                logVO.setExcName(e.getClass().getName());
                logVO.setExcInfo(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace(), handlerConfig));
            }
            //请求的参数，参数所在的数组转换成json
            logVO.setParams(Arrays.toString(jp.getArgs()));
            //返回值
            if(null != keys && Void.class.getName() != keys){
                StringBuilder result = new StringBuilder(handlerConfig.getObjectMapper().writeValueAsString(keys));
                if (handlerConfig.getResultLength() == 0){
                    //表示全部
                    logVO.setReturnValue(result.toString());
                } else {
                    String tempResult = result.substring(0, handlerConfig.getResultLength());
                    logVO.setReturnValue(tempResult);
                }
            }
        } catch (Exception ignored) {

        }
        doLog(e, logVO, handlerConfig);
    }

    protected void copyAnnotationValue(BrokyLog anno, BrokyLogVO logVO) {
        logVO.setModel(anno.module());
        logVO.setOptType(anno.optType());
        logVO.setDescription(anno.description());
    }

    protected boolean needLog(Throwable e, BrokyLogHandlerConfig handlerConfig) {
        return null != e && handlerConfig.getEndAt() - handlerConfig.getStartAt() < handlerConfig.getRunTime();
    }

    protected void setRuntimeFromAnnotation(BrokyLog anno, Method method, BrokyLogHandlerConfig handlerConfig) {
        if (!"-1".equals(anno.runTime())) {
            try {
                handlerConfig.setRunTime(Long.parseLong(anno.runTime()));
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException("方法" + method.getName() + "的注解参数runtime必须是整数");
            }
        }
    }

    protected void doLog(Throwable e, BrokyLogVO logVO, BrokyLogHandlerConfig handlerConfig) {
        if (null == e) {
            long et = handlerConfig.getEndAt() - handlerConfig.getStartAt();
            if (et < handlerConfig.getRunTime()) {
                return;
            }
            logVO.setExecTime(et);
            log.info(logVO.toString());
        } else {
            if (pool.exist(e.getClass())) {
                handlerConfig.setExcFullShow(false);
                logVO.setExecTime(handlerConfig.getEndAt() - handlerConfig.getStartAt());
                log.warn(logVO.toString());
                return;
            }
            logVO.setExecTime(-1L);
            log.error(logVO.toString());
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


}
