package com.emily.infrastructure.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.spring.boot.autoconfigure.stat.DruidSpringAopConfiguration;
import com.emily.infrastructure.common.constant.AopOrderInfo;
import com.emily.infrastructure.core.aop.advisor.AnnotationPointcutAdvisor;
import com.emily.infrastructure.datasource.annotation.TargetDataSource;
import com.emily.infrastructure.datasource.context.DynamicMultipleDataSources;
import com.emily.infrastructure.datasource.exception.DataSourceNotFoundException;
import com.emily.infrastructure.datasource.interceptor.DataSourceCustomizer;
import com.emily.infrastructure.datasource.interceptor.DataSourceMethodInterceptor;
import com.emily.infrastructure.logger.LoggerFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: 控制器切点配置
 * @Author Emily
 * @Version: 1.0
 */
@Configuration
@AutoConfigureBefore(DruidDataSourceAutoConfigure.class)
@EnableConfigurationProperties(DataSourceProperties.class)
@ConditionalOnProperty(prefix = DataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class DataSourceAutoConfiguration implements BeanFactoryPostProcessor, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceAutoConfiguration.class);


    /**
     * 数据源切面增强类，支持@TargetDataSource注解标注在父类、接口、父类或接口的方法上都可以拦截到
     *
     * @param dataSourceCustomizers 切面|拦截器
     * @param properties            属性配置
     * @return 切面增强类
     * @since(4.0.6)
     */
    @Bean
    @ConditionalOnBean(DataSourceCustomizer.class)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public Advisor dataSourcePointCutAdvice(ObjectProvider<DataSourceCustomizer> dataSourceCustomizers, DataSourceProperties properties) {
        //限定类级别的切点
        Pointcut cpc = new AnnotationMatchingPointcut(TargetDataSource.class, properties.isCheckInherited());
        //限定方法级别的切点
        Pointcut mpc = new AnnotationMatchingPointcut(null, TargetDataSource.class, properties.isCheckInherited());
        //组合切面(并集)，即只要有一个切点的条件符合，则就拦截
        Pointcut pointcut = new ComposablePointcut(cpc).union(mpc);
        //切面增强类
        AnnotationPointcutAdvisor advisor = new AnnotationPointcutAdvisor(dataSourceCustomizers.orderedStream().findFirst().get(), pointcut);
        //切面优先级顺序
        advisor.setOrder(AopOrderInfo.DATASOURCE);
        return advisor;
    }

    @Bean
    @Order(AopOrderInfo.DATASOURCE_INTERCEPTOR)
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DataSourceMethodInterceptor dataSourceMethodInterceptor(DataSourceProperties dataSourceProperties) {
        return new DataSourceMethodInterceptor(dataSourceProperties);
    }

    /**
     * 从配置文件中获取多数据源配置信息
     * {@link DataSourceTransactionManagerAutoConfiguration}
     * {@link MybatisAutoConfiguration}
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DataSource dynamicMultipleDataSources(DataSourceProperties dataSourceProperties) {
        Map<String, DruidDataSource> configs = dataSourceProperties.getConfig();
        if (Objects.isNull(dataSourceProperties.getDefaultConfig())) {
            throw new DataSourceNotFoundException("默认数据库必须配置");
        }
        if (configs.isEmpty()) {
            throw new DataSourceNotFoundException("数据库配置不存在");
        }
        Map<Object, Object> targetDataSources = new HashMap<>(configs.size());
        configs.keySet().forEach(key -> targetDataSources.put(key, configs.get(key)));
        return DynamicMultipleDataSources.build(dataSourceProperties.getDefaultDataSource(), targetDataSources);
    }

    /**
     * 将指定的bean 角色标记为基础设施类型，相关提示类在 org.springframework.context.support.PostProcessorRegistrationDelegate
     *
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String beanName = MessageFormat.format("{0}-{1}", DataSourceProperties.PREFIX, DataSourceProperties.class.getName());
        if (beanFactory.containsBeanDefinition(beanName)) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }

        beanName = DruidSpringAopConfiguration.class.getName();
        if (beanFactory.containsBeanDefinition(beanName)) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }

        beanName = MessageFormat.format("{0}-{1}", "spring.datasource.druid", DruidStatProperties.class.getName());
        if (beanFactory.containsBeanDefinition(beanName)) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        }

    }

    @Override
    public void destroy() {
        logger.info("<== 【销毁--自动化配置】----数据库多数据源组件【DataSourceAutoConfiguration】");
    }

    @Override
    public void afterPropertiesSet() {
        logger.info("==> 【初始化--自动化配置】----数据库多数据源组件【DataSourceAutoConfiguration】");
    }
}
