package hello.aop.exam.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class RetryAspect {

	@Around("@annotation(retry)")
	public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
		log.info("[retry] {} args={}", joinPoint.getSignature(), retry);

		int maxRetry = retry.value();
		Exception exceptionHolder = null; // 예외 저장

		for (int retryCount = 1; retryCount <= maxRetry; retryCount++) {
			try {
				log.info("[retry] try count={}/{}", retryCount, maxRetry);
				return joinPoint.proceed();
			} catch (Exception e) {
				exceptionHolder = e; // 예외를 저장하고 재시도를 위해 다시 루프를 돈다
			}
		}
		throw exceptionHolder;
	}

}
