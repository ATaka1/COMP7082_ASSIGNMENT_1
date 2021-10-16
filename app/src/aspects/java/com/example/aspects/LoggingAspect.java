package com.example.aspects;

import android.util.Log;
import com.example.libs.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggingAspect {
    private Logger logger = Logger.getInstance();


    @Pointcut("execution(* com.example.comp7082_assignment_1.View.*..*(..))")
        public void viewOperations() {
    }
    @After("viewOperations()")
    public Object profileView(JoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        String tag = point.getTarget().getClass().toString();
        Object output = null;
        StringBuilder logMsg = new StringBuilder("Class:"+point.getTarget().getClass()+" entry -> method ->"+point.getSignature().getName());

        logMsg.append(System.getProperty("line.separator"));
        Log.i(tag,"Class:"+point.getTarget().getClass()+" entry -> method ->"+point.getSignature().getName());
        try {
            long elapsedTime = System.currentTimeMillis() - start;
            logMsg.append("Method execution time: " + elapsedTime + " milliseconds.");
            logMsg.append(System.getProperty("line.separator"));

            Log.i(tag, "Method execution time: " + elapsedTime + " milliseconds.");
            logMsg.append("Class:"+point.getTarget().getClass()+" exit -> method ->"+point.getSignature().getName());

            logMsg.append(System.getProperty("line.separator"));
            Log.i(tag, "Class:"+point.getTarget().getClass()+" exit -> method ->"+point.getSignature().getName());
        } catch (Throwable t) {
            throw new InternalError(t.getMessage());
        }
        logger.appendLog(logMsg.toString());
        return output;
    }
    @Pointcut("execution(* com.example.comp7082_assignment_1.Presenter.*..*(..))")
    public void presenterOperations() {
    }
    @After("presenterOperations()")
    public Object profilePresenter(JoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        String tag = point.getTarget().getClass().toString();
        Object output = null;
        StringBuilder logMsg = new StringBuilder("Class:"+point.getTarget().getClass()+" entry -> method ->"+point.getSignature().getName());
        logMsg.append(System.getProperty("line.separator"));
        Log.i(tag,"Class:"+point.getTarget().getClass()+" entry -> method ->"+point.getSignature().getName());
        try {
            long elapsedTime = System.currentTimeMillis() - start;
            logMsg.append("Method execution time: " + elapsedTime + " milliseconds.");
            logMsg.append(System.getProperty("line.separator"));

            Log.i(tag, "Method execution time: " + elapsedTime + " milliseconds.");
            logMsg.append("Class:"+point.getTarget().getClass()+" exit -> method ->"+point.getSignature().getName());

            logMsg.append(System.getProperty("line.separator"));
            Log.i(tag, "Class:"+point.getTarget().getClass()+" exit -> method ->"+point.getSignature().getName());
        } catch (Throwable t) {
            throw new InternalError(t.getMessage());
        }
        logger.appendLog(logMsg.toString());
        return output;
    }
}
