package hello.jdbc.exception.basic;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class UncheckedTest {

	@DisplayName("Unchecked Exception을 잡아서 처리하기")
	@Test
	void unchecked_catch() {
		Service service = new Service();
		service.callCatch();
	}

	@DisplayName("Unchecked Exception을 밖으로 던지기")
	@Test
	void unchecked_throw() {
		Service service = new Service();
		assertThatThrownBy(service::callThrow)
			.isInstanceOf(MyUncheckedException.class);
	}

	/**
	 * RuntimeException을 상속받은 예외는 Unchecked Exception
	 */
	static class MyUncheckedException extends RuntimeException {

		public MyUncheckedException(String message) {
			super(message);
		}

	}

	/**
	 * Unchecked Exception
	 * 예외를 잡거나, 던지지 않아도 된다.
	 * 예외를 잡지 않으면 자동으로 밖으로 던진다.
	 */
	static class Service {

		Repository repository = new Repository();

		/**
		 * 필요한 경우 예외를 잡아서 처리
		 */
		public void callCatch() {
			try {
				repository.call();
			} catch (MyUncheckedException e) {
				log.info("예외 처리, message={}", e.getMessage(), e);
			}
		}

		/**
		 * 예외를 잡지 않아도 Unchecked Exception은 자연스럽게 상위로 넘어간다.
		 * throws 예외 선언을 하지 않아도 된다.
		 * 중요한 예외의 경우 throws 예외 선언을 통해 IDE를 통해서 인지할 수 있도록 하는 것이 좋다.
		 */
		public void callThrow() {
			repository.call();
		}

	}

	static class Repository {

		public void call() {
			throw new MyUncheckedException("ex");
		}

	}

}
