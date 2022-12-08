package cn.cimoc.broky.core;

/**
 * @author LGZ
 *
 */
public class BrokyUtils {
    public static BrokyResult ajaxReturn(Integer errCode, String errMsg, Object data) {
        return new BrokyResult(errCode, errMsg, data);
    }

    public static BrokyResult ajaxReturn(BrokyError error) {
        return new BrokyError(error.getErrCode(), error.getErrMsg(), null);
    }

    public static BrokyResult ajaxReturn(Object data) {
        return new BrokyResult(200, "成功", data);
    }

    public static BrokyResult ajaxReturn(Integer code, String message){
        return new BrokyResult(code, message, null);
    }

    public static void success() {
        throw new BrokyException(BrokyError.SUCCESS);
    }

    public static void fail(BrokyError error) {
        throw new BrokyException(error);
    }
}
