package com.zy.foundation.lang.annotation.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import java.util.Arrays;

/**
 * 1.该类需要被 spring 管理才有效
 * 2.请处理好项目中的日志依赖
 */
@Aspect
@Slf4j
public class TraceAdvice {

    @Around("within(Trace) || @annotation(Trace)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        MethodSignature signature = (MethodSignature) point.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        log.info("input info: {}, {}, {}", className, methodName, Arrays.toString(args));
        Object result = point.proceed();
        log.info("output info: {}, {}, {}, {}", className, methodName, Arrays.toString(args), result);
        return result;
    }

}
