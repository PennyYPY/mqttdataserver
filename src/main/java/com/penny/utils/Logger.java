package com.penny.utils;

import org.springframework.integration.handler.LoggingHandler;

/**
 * Created by Penny on 2017/11/9.
 */

/**
 * 打印日志类
 * */
public class Logger {

    private LoggingHandler logger(){

        LoggingHandler loggingHandler = new LoggingHandler("INFO");
        return loggingHandler;


    }

}
