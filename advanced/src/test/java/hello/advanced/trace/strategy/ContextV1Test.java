package hello.advanced.trace.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ContextV1Test {

	@Test
	void strategyV0() {
		logic1();
		logic2();
	}

	private void logic1() {
		long startTime = System.currentTimeMillis();

		// 비즈니스 로직 실행
		log.info("비즈니스 로직1 실행");
		// 비즈니스 로직 종료

		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("resultTime={}", resultTime);
	}

	private void logic2() {
		long startTime = System.currentTimeMillis();

		// 비즈니스 로직 실행
		log.info("비즈니스 로직2 실행");
		// 비즈니스 로직 종료

		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("resultTime={}", resultTime);
	}

	/**
	 * 전략 패턴 사용
	 */
	@DisplayName("전략 패턴 사용: 필드에 전략을 보관")
	@Test
	void strategyV1() {
		StrategyLogic1 strategyLogic1 = new StrategyLogic1();
		ContextV1 context1 = new ContextV1(strategyLogic1);
		context1.execute();

		StrategyLogic2 strategyLogic2 = new StrategyLogic2();
		ContextV1 context2 = new ContextV1(strategyLogic2);
		context2.execute();
	}

}
