package hello.proxy.cglib.code;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

	private final Object target; // 프록시는 호출할 대상 클래스가 무조건 필요

	public TimeMethodInterceptor(Object target) {
		this.target = target;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		log.info("TimeProxy 실행");
		long startTime = System.currentTimeMillis();

		Object result = methodProxy.invoke(target, args);

		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("TimeProxy 종료 resultTime={}", resultTime);
		return result;
	}

}
