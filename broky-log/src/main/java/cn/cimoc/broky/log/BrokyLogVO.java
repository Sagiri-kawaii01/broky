package cn.cimoc.broky.log;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LGZ
 * <p>
 */
@Data
public class BrokyLogVO implements Serializable {
    /**
     * 请求的类
     */
    private String className;

    /**
     * 请求的方法
     */
    private String methodName;

    /**
     * 请求的参数
     */
    private String params;

    /**
     * 返回值
     */
    private String returnValue;

    /**
     * 模块，从@BrokyLog里拿
     */
    private String model;

    /**
     * 操作类型，从@BrokyLog里拿
     */
    private String optType;

    /**
     * 操作说明，从@BrokyLog里拿
     */
    private String description;

    /**
     * 请求的路径
     */
    private String uri;

    /**
     * 请求的ip地址
     */
    private String ip;

    /**
     * 请求的时间
     */
    private String reqTime;

    /**
     * 执行的时长
     */
    private Long execTime;

    /**
     * 异常名称
     */
    private String excName;

    /**
     * 异常信息
     */
    private String excInfo;
}
