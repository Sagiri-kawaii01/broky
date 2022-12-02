package cn.cimoc.broky.core;

/**
 * @author LGZ
 * <p>
 */
public class BrokyUtils {
    public static BrokyResult ajaxReturn(Integer errCode, String errMsg, Object data) {
        return new BrokyResult(errCode, errMsg, data);
    }

    public static BrokyResult ajaxReturn(Object data) {
        return new BrokyResult(200, "成功", data);
    }

    public static BrokyResult ajaxReturn(Integer code, String message){
        return new BrokyResult(code, message, null);
    }
}
