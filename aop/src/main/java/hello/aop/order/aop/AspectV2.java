package hello.aop.order.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class AspectV2 {

	// 적용 대상: hello.aop.order 패키지와 그 하위 패키지
	@Pointcut("execution(* hello.aop.order..*(..))") // pointcut expression
	private void allOrder() { // pointcut signature
	}

	@Around("allOrder()")
	public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
		return joinPoint.proceed();
	}

}
