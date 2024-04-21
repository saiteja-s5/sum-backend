package building.sum.inventory.advice;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAdvice {

	private static final Logger log = LogManager.getLogger();

	@Value("${sum.inventory.method-in-out-logs-enabled}")
	private boolean methodInOutLogsEnabled;

	@Value("${sum.inventory.method-execution-time-logs-enabled}")
	private boolean methodExecutionTimeLogsEnabled;

	@Pointcut("within(building.sum.inventory.controller..*) " + "|| within(building.sum.inventory.service.impl..*) "
			+ "|| within(building.sum.inventory.repository..*)")
	public void packagePointCut() {
	}

	@Around("packagePointCut()")
	public Object logEntryExit(ProceedingJoinPoint joinPoint) throws Throwable {
		if (methodInOutLogsEnabled) {
			String entryLog = String.format("Entering:[%s.%s()] with argument[s] = [%s]",
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
					Arrays.toString(joinPoint.getArgs()));
			log.info(entryLog);
			Object result = joinPoint.proceed();
			String exitLog = String.format("Exiting:[%s.%s()] with result = [%s]",
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), result);
			log.info(exitLog);
			return result;
		} else {
			return joinPoint.proceed();
		}
	}

	@Around("packagePointCut()")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		if (methodExecutionTimeLogsEnabled) {
			final StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			Object result = joinPoint.proceed();
			stopWatch.stop();
			String executionLog = String.format("Execution time taken for [%s.%s()] is [%d] ms",
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
					stopWatch.getTotalTimeMillis());
			log.info(executionLog);
			return result;
		} else {
			return joinPoint.proceed();
		}
	}

}
