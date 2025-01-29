package org.example.tms.aspect.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.tms.exception.MethodExecutionException;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MethodExecutionTimeAspect {

    /**
     * Logs the execution time of methods annotated with @ExecutionTime.
     *
     * @param proceedingJoinPoint The join point representing the method execution.
     * @return The result of the method execution.
     * @throws MethodExecutionException If the method execution fails.
     */
    @Around("@annotation(org.example.tms.aspect.logging.annotation.ExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) {
        var methodName = proceedingJoinPoint.getSignature()
                .toShortString();
        long startTime = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            log.debug("Execution of method '{}' started at: {}", methodName, startTime);
        } else {
            log.info("Execution of method '{}' started", methodName);
        }

        Object result;
        try {
            result = proceedingJoinPoint.proceed();

        } catch (Throwable e) {
            log.error("Execution of method '{}' failed due to an error", methodName, e);
            throw new MethodExecutionException(methodName, e);

        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            if (log.isDebugEnabled()) {
                log.debug("Execution of method '{}' ended at: {} (Duration: {} ms)", methodName, endTime, duration);
            } else {
                log.info("Execution of method '{}' took {} ms", methodName, duration);
            }
        }
        return result;
    }
}
