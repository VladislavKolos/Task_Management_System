package org.example.tms.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.tms.exception.custom.TransactionExecutionException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@Aspect
@Component
public class TransactionalAuditAspect {

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object logTransactionalAudit(ProceedingJoinPoint proceedingJoinPoint) {
        String methodName = proceedingJoinPoint.getSignature()
                .getName();
        Object[] args = proceedingJoinPoint.getArgs();
        log.info("Executing method: {} with arguments: {}", methodName, args);

        try {
            Object result = proceedingJoinPoint.proceed();
            log.info("Method {} executed successfully with result: {}", methodName, result);

            return result;

        } catch (SQLException e) {
            log.error("SQL Exception occurred while executing method {}: {}", methodName, e.getMessage(), e);

            throw new TransactionExecutionException("Database error while executing method: " + methodName, e);

        } catch (IOException e) {
            log.error("I/O Exception occurred while executing method {}: {}", methodName, e.getMessage(), e);

            throw new TransactionExecutionException("I/O error while executing method: " + methodName, e);

        } catch (Exception e) {
            log.error("Exception occurred while executing method {}: {}", methodName, e.getMessage(), e);

            throw new TransactionExecutionException("Error occurred while executing method: " + methodName, e);

        } catch (Throwable e) {
            log.error("Unexpected error occurred while executing method {}: {}", methodName, e.getMessage(), e);

            throw new RuntimeException("Unexpected error while executing method: " + methodName, e);
        }
    }
}
