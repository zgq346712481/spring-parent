package com.sgrain.boot.autoconfigure.initializers;

import com.sgrain.boot.common.enums.DateFormatEnum;
import com.sgrain.boot.common.utils.date.DateUtils;
import com.sgrain.boot.common.utils.log.LoggerUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

import java.util.Date;

/**
 * @program: spring-parent
 * @description: 小米粒框架初始化器
 * @create: 2020/09/22
 */
public class GrainApplicationContenxtInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        LoggerUtils.info(GrainApplicationContenxtInitializer.class, "Small Grain【小米粒】初始化器开始初始化IOC容器了,容器名为：" +
                applicationContext.getClass().getSimpleName() + "--当前时间是：" + DateUtils.formatDate(new Date(), DateFormatEnum.YYYY_MM_DD_HH_MM_SS.getFormat()));
    }
}
