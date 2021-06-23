package com.emily.infrastructure.logback.builder;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.emily.infrastructure.common.utils.path.PathUtils;
import com.emily.infrastructure.logback.LogbackProperties;
import com.emily.infrastructure.logback.appender.LogbackAsyncAppender;
import com.emily.infrastructure.logback.appender.LogbackConsoleAppender;
import com.emily.infrastructure.logback.appender.LogbackRollingFileAppender;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Emily
 * @program: spring-parent
 * @description: 日志类 logback+slf4j
 * @create: 2020/08/04
 */
public class LogbackBuilder {
    /**
     * Logger对象容器
     */
    private static final Map<String, Logger> loggerCache = new ConcurrentHashMap<>();

    private LogbackProperties properties;

    public LogbackBuilder(LogbackProperties properties) {
        this.properties = properties;
    }

    /**
     * 获取日志输出对象
     *
     * @param fileName 日志文件名|模块名称
     * @return
     */
    public Logger getLogger(Class cls, String path, String fileName) {
        /**
         * 路径地址标准化
         */
        path = PathUtils.normalizePath(path);
        /**
         * logger对象name
         */
        String loggerName = cls.getName();

        Logger logger = loggerCache.get(loggerName);
        if (Objects.nonNull(logger)) {
            return logger;
        }
        synchronized (this) {
            logger = loggerCache.get(loggerName);
            if (Objects.nonNull(logger)) {
                return logger;
            }
            logger = builder(loggerName, path, fileName);
            loggerCache.put(loggerName, logger);
        }
        return logger;

    }

    /**
     * 构建RootLogger对象，需在配置类中主动调用进行初始化
     * 日志级别以及优先级排序: OFF > ERROR > WARN > INFO > DEBUG > TRACE >ALL
     */
    protected void initRootLogger() {
        String appenderName = Logger.ROOT_LOGGER_NAME;
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        LogbackRollingFileAppender rollingFileAppender = new LogbackRollingFileAppender(loggerContext, properties);
        //设置是否向上级打印信息
        logger.setAdditive(false);
        // 配置日志级别
        Level level = Level.toLevel(properties.getLevel().levelStr);
        if (properties.isEnableAsyncAppender()) {
            LogbackAsyncAppender asyncAppender = new LogbackAsyncAppender(loggerContext, properties);
            if (level.levelInt <= Level.ERROR_INT) {
                logger.addAppender(asyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.ERROR.levelStr.toLowerCase(), Level.ERROR)));
            }
            if (level.levelInt <= Level.WARN_INT) {
                logger.addAppender(asyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.WARN.levelStr.toLowerCase(), Level.WARN)));
            }
            if (level.levelInt <= Level.INFO_INT) {
                logger.addAppender(asyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.INFO.levelStr.toLowerCase(), Level.INFO)));
            }
            if (level.levelInt <= Level.DEBUG_INT) {
                logger.addAppender(asyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.DEBUG.levelStr.toLowerCase(), Level.DEBUG)));
            }
            if (level.levelInt <= Level.TRACE_INT) {
                logger.addAppender(asyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.TRACE.levelStr.toLowerCase(), Level.TRACE)));
            }
        } else {
            if (level.levelInt <= Level.ERROR_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.ERROR.levelStr.toLowerCase(), Level.ERROR));
            }
            if (level.levelInt <= Level.WARN_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.WARN.levelStr.toLowerCase(), Level.WARN));
            }
            if (level.levelInt <= Level.INFO_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.INFO.levelStr.toLowerCase(), Level.INFO));
            }
            if (level.levelInt <= Level.DEBUG_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.DEBUG.levelStr.toLowerCase(), Level.DEBUG));
            }
            if (level.levelInt <= Level.TRACE_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(appenderName, null, Level.TRACE.levelStr.toLowerCase(), Level.TRACE));
            }
        }
        // 添加控制台appender
        logger.addAppender(new LogbackConsoleAppender(loggerContext, properties).getConsoleAppender(level));
        // 设置日志级别
        logger.setLevel(level);
    }

    /**
     * 构建Logger对象
     * 日志级别以及优先级排序: OFF > ERROR > WARN > INFO > DEBUG > TRACE >ALL
     *
     * @param fileName 日志文件名|模块名称
     * @return
     */
    protected Logger builder(String name, String path, String fileName) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(name);
        /**
         * 设置是否向上级打印信息
         */
        logger.setAdditive(false);
        // 配置日志级别
        Level level = Level.toLevel(properties.getLevel().levelStr);
        LogbackRollingFileAppender rollingFileAppender = new LogbackRollingFileAppender(loggerContext, properties);
        //是否开启异步日志
        if (properties.isEnableAsyncAppender()) {
            LogbackAsyncAppender logbackAsyncAppender = new LogbackAsyncAppender(loggerContext, properties);
            if (level.levelInt <= Level.ERROR_INT) {
                logger.addAppender(logbackAsyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.ERROR)));
            }
            if (level.levelInt <= Level.WARN_INT) {
                logger.addAppender(logbackAsyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.WARN)));
            }
            if (level.levelInt <= Level.INFO_INT) {
                logger.addAppender(logbackAsyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.INFO)));
            }
            if (level.levelInt <= Level.DEBUG_INT) {
                logger.addAppender(logbackAsyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.DEBUG)));
            }
            if (level.levelInt <= Level.TRACE_INT) {
                logger.addAppender(logbackAsyncAppender.getAsyncAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.TRACE)));
            }
        } else {
            if (level.levelInt <= Level.ERROR_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.ERROR));
            }
            if (level.levelInt <= Level.WARN_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.WARN));
            }
            if (level.levelInt <= Level.INFO_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.INFO));
            }
            if (level.levelInt <= Level.DEBUG_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.DEBUG));
            }
            if (level.levelInt <= Level.TRACE_INT) {
                logger.addAppender(rollingFileAppender.getRollingFileAppender(name, path, fileName, Level.TRACE));
            }
        }
        if (properties.isEnableModuleConsole()) {
            // 添加控制台appender
            logger.addAppender(new LogbackConsoleAppender(loggerContext, properties).getConsoleAppender(level));
        }
        // 设置日志级别
        logger.setLevel(level);
        return logger;
    }


}