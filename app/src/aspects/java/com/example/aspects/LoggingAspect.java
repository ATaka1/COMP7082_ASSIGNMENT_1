package com.example.aspects;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.libs.Logger;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {
    private Logger logger = Logger.getInstance();

    private static final String TAG = "MyActivity";

//    @Pointcut("execution(* onCreate(..))")
//    public void onButtonClick() {
//        logger.appendLog("Button Clicked");
//        Log.i(TAG,"@PointCut On Button CLick");
//    }
//
//    @Before("onButtonClick() && args(view)")
//    public void onButtonClickLog(View v) {
//        if(v instanceof TextView) {
//            String text = ((TextView) v).getText().toString();
//            logger.appendLog(text);
//        }
//        Log.i(v.toString(),"@Before On Button CLick");
//    }

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
