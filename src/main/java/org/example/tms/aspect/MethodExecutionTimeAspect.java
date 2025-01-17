package org.example.tms.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.tms.exception.custom.MethodExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MethodExecutionTimeAspect {

    @Around("@annotation(org.example.tms.annotation.ExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) {
        long startTime = System.currentTimeMillis();
        String methodName = proceedingJoinPoint.getSignature()
                .toShortString();
        log.info("Execution of method '{}' started at: {}", methodName, startTime);

        Object result;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error("Method '{}' execution failed", methodName, e);
            throw new MethodExecutionException("Execution of method '" + methodName + "' failed", e);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.info("Execution of method '{}' ended at: {} (Duration: {} ms)", methodName, endTime, duration);

        return result;
    }
}
