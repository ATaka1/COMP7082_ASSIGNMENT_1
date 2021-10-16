package com.example.aspects;
import android.util.Log;

import com.example.libs.Logger;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {
    private Logger logger = Logger.getInstance();

    private static final String TAG = "MyActivity";
    @Before("call(void java.io.PrintStream.println(String)) " +
            "&& !within(com.example.aspects..*)")
    public void beforePrintlnCall() {
        logger.appendLog("Before Print Call");
    }

    @After("call(void java.io.PrintStream.println(String)) " +
            "&&  !within(com.example.aspects..*)")
    public void afterPrintlnCall() {
        logger.appendLog("Just made call to print Hello World");
    }
}
