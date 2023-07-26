package hello.proxy.cglib;

import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CglibTest {

	@Test
	void cglib() {
		ConcreteService target = new ConcreteService();

		Enhancer enhancer = new Enhancer(); // CGLIB를 만드는 코드
		enhancer.setSuperclass(ConcreteService.class); // 프록시 생성을 위한 상위 클래스 지정
		enhancer.setCallback(new TimeMethodInterceptor(target)); // 프록시에 적용할 실행 로직
		ConcreteService proxy = (ConcreteService)enhancer.create(); // 프록시 생성
		log.info("targetClass={}", target.getClass());
		log.info("proxyClass={}", proxy.getClass());

		proxy.call();
	}

}
