package com.emily.infrastructure.logger;

import com.emily.infrastructure.logback.LogbackProperties;
import com.emily.infrastructure.logback.configuration.context.LogbackContext;
import com.emily.infrastructure.logback.configuration.enumeration.LogbackType;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 * @author Emily
 * @Description: 日志工具类 日志级别总共有TARCE < DEBUG < INFO < WARN < ERROR < FATAL，且级别是逐渐提供，
 * 如果日志级别设置为INFO，则意味TRACE和DEBUG级别的日志都看不到。
 * @Version: 1.0
 */
public class LoggerFactory {
    /**
     * 加锁对象
     */
    private static Object lock = new Object();
    /**
     * Logger日志上下文
     */
    public static LogbackContext CONTEXT;
    /**
     * 容器上下文
     */
    public static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 获取日志Logger对象
     *
     * @param clazz 类实例
     * @param <T>
     * @return
     */
    public static <T> Logger getLogger(Class<T> clazz) {
        return org.slf4j.LoggerFactory.getLogger(clazz);
    }

    /**
     * 获取分组Logger日志对象
     *
     * @param clazz    类实例对象
     * @param filePath 日志文件路径
     * @param <T>
     * @return
     */
    public static <T> Logger getGroupLogger(Class<T> clazz, String filePath) {
        return getGroupLogger(clazz, filePath, null);
    }

    /**
     * 获取分组Logger日志对象
     *
     * @param clazz    类实例
     * @param filePath 日志文件对象
     * @param fileName 文件名
     * @param <T>
     * @return
     */
    public static <T> Logger getGroupLogger(Class<T> clazz, String filePath, String fileName) {
        validLoggerContext();
        return CONTEXT.getLogger(clazz, filePath, fileName, LogbackType.GROUP);
    }

    /**
     * 获取模块Logger日志对象
     *
     * @param clazz    类实例
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param <T>
     * @return
     */
    public static <T> Logger getModuleLogger(Class<T> clazz, String filePath, String fileName) {
        validLoggerContext();
        return CONTEXT.getLogger(clazz, filePath, fileName, LogbackType.MODULE);
    }

    /**
     * 校验Logger上下文的有效性
     */
    private static void validLoggerContext() {
        if (CONTEXT == null) {
            synchronized (lock) {
                String[] beanNamesForType = APPLICATION_CONTEXT.getBeanNamesForType(LogbackProperties.class);
                if (beanNamesForType.length > 0) {
                    CONTEXT = new LogbackContext(APPLICATION_CONTEXT.getBean(LogbackProperties.class));
                } else {
                    CONTEXT = new LogbackContext(new LogbackProperties());
                }
            }
        }
        if (CONTEXT == null) {
            throw new RuntimeException("logback日志组件上下文未初始化");
        }
    }
}
