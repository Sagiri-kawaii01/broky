package cn.cimoc.broky.core;

import java.io.Serializable;

/**
 * @author LGZ
 * <p>
 */
public class BrokyError extends BrokyResult implements Serializable {

    public static final BrokyError SUCCESS = new BrokyError(200, "成功");
    public static final BrokyError PARAM_ERROR = new BrokyError(4001, "参数异常，请检查格式是否正确");
    public static final BrokyError PARAM_VALID_ERROR = new BrokyError(4002, "参数校验不通过，请检查格式是否正确");
    public static final BrokyError UNKNOWN_ERROR = new BrokyError(500, "服务端发生未知错误，请联系开发人员");

    public BrokyError(Integer errCode, String errMsg) {
        this(errCode, errMsg, null);
    }

    public BrokyError(Integer errCode, String errMsg, Object data) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.data = data;
    }
}
