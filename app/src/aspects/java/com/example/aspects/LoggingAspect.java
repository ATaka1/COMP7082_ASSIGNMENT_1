package com.example.aspects;
import android.util.Log;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {
    private static final String TAG = "MyActivity";
    @Before("   call(void java.io.PrintStream.println(String)) " +
            "&& !within(com.example.aspects..*)")
    public void beforePrintlnCall() {

        Log.i(TAG, "Print Call");
    }

    @After("    call(void java.io.PrintStream.println(String)) " +
            "&&  !within(com.example.aspects..*)")
    public void afterPrintlnCall() {
        System.out.println("Just made call to print Hello World");
    }
}
