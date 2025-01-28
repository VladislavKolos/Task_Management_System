package org.example.tms.aspect.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.tms.exception.TransactionExecutionException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@Aspect
@Component
public class TransactionalAuditAspect {

    /**
     * Logs the execution of transactional methods and handles exceptions thrown during their execution.
     *
     * @param proceedingJoinPoint The join point representing the method execution.
     * @return The result of the method execution.
     * @throws TransactionExecutionException If any exceptions related to transaction execution occur.
     */
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
            throw new TransactionExecutionException(
                    TransactionExecutionException.ErrorType.DATABASE_ERROR_EXECUTING_METHOD, methodName, e);

        } catch (IOException e) {
            log.error("I/O Exception occurred while executing method {}: {}", methodName, e.getMessage(), e);
            throw new TransactionExecutionException(TransactionExecutionException.ErrorType.IO_ERROR_EXECUTING_METHOD,
                    methodName, e);

        } catch (Exception e) {
            log.error("Exception occurred while executing method {}: {}", methodName, e.getMessage(), e);
            throw new TransactionExecutionException(
                    TransactionExecutionException.ErrorType.GENERIC_ERROR_EXECUTING_METHOD, methodName, e);

        } catch (Throwable e) {
            log.error("Unexpected error occurred while executing method {}: {}", methodName, e.getMessage(), e);
            throw new RuntimeException("Unexpected error while executing method: " + methodName, e);
        }
    }
}
