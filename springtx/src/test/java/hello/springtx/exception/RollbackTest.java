package hello.springtx.exception;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
class RollbackTest {

	@Autowired
	RollbackService service;

	@DisplayName("RuntimeException: 트랜잭션 롤백")
	@Test
	void runtimeException() {
		assertThatThrownBy(() -> service.runtimeException())
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("Checked Exception: 트랜잭션 커밋")
	@Test
	void checkedException() {
		assertThatThrownBy(() -> service.checkedException())
			.isInstanceOf(MyException.class);
	}

	@DisplayName("rollbackFor Checked Exception: 트랜잭션 강제 롤백")
	@Test
	void rollbackFor() {
		assertThatThrownBy(() -> service.rollbackFor())
			.isInstanceOf(MyException.class);
	}

	@TestConfiguration
	static class RollbackTestConfig {

		@Bean
		RollbackService rollbackService() {
			return new RollbackService();
		}

	}

	@Slf4j
	static class RollbackService {

		// 런타임 예외 발생: 롤백
		@Transactional
		public void runtimeException() {
			log.info("call runtimeException");
			throw new RuntimeException();
		}

		// 체크 예외 발생: 커밋
		@Transactional
		public void checkedException() throws MyException {
			log.info("call checkedException");
			throw new MyException();
		}

		// 체크 예외 rollbackFor 지정: 롤백
		@Transactional(rollbackFor = MyException.class)
		public void rollbackFor() throws MyException {
			log.info("call rollbackFor");
			throw new MyException();
		}

	}

	static class MyException extends Exception {

	}

}
