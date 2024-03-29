package hello.aop.internalcall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CallServiceV1 {

	private CallServiceV1 callServiceV1;

	/**
	 * 참고: 생성자 주입은 순환 사이클을 만들기 때문에 실패한다.
	 * setter를 통해 주입을 받아야 순환 참조 문제가 발생하지 않는다.
	 */
	@Autowired
	public void setCallServiceV1(CallServiceV1 callServiceV1) {
		// log.info("callServiceV1 setter={}", callServiceV1.getClass()); // 프록시가 주입 된다
		this.callServiceV1 = callServiceV1;
	}

	public void external() {
		log.info("call external");
		callServiceV1.internal(); // 외부 메서드 호출
	}

	public void internal() {
		log.info("call internal");
	}

}
