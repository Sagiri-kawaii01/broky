package cn.cimoc.broky.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author LGZ
 * <p>
 */
@Data
@ConfigurationProperties(prefix = BrokyLogProperties.PREFIX)
public class BrokyLogProperties {

    public static final String PREFIX = "broky.log";

    /**
     * 是否启用日志
     */
    private Boolean enable = true;

    /**
     * 方法的运行时长  当方法的运行时间 >= 设置的值时，才记录。 默认为
     */
    private Long runTime = 0L;

    /**
     * 异常的堆栈信息 是否全部展示
     */
    private Boolean excFullShow = false;

    /**
     * 输出结果的长度  0 表示全部输出
     */
    private Integer resultLength = 0;
}
