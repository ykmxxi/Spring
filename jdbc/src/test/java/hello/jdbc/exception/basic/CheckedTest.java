package hello.jdbc.exception.basic;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class CheckedTest {

	@DisplayName("Checked Exception을 잡아서 처리하기")
	@Test
	void checked_catch() {
		Service service = new Service();
		service.callCatch();
	}

	@DisplayName("Checked Exception을 밖으로 던지기")
	@Test
	void checked_throw() {
		Service service = new Service();
		assertThatThrownBy(service::callThrow)
			.isInstanceOf(MyCheckedException.class);
	}

	/**
	 * Exception을 상속받은 예외는 Checked Exception
	 */
	static class MyCheckedException extends Exception {

		public MyCheckedException(String message) {
			super(message);
		}

	}

	/**
	 * Checked Exception
	 * 예외를 잡아서 처리하거나, 던지거나 둘중 하나로 처리해야 한다.
	 */
	static class Service {

		Repository repository = new Repository();

		/**
		 * 예외를 잡아서 처리하는 코드
		 */
		public void callCatch() {
			try {
				repository.call();
			} catch (MyCheckedException e) {
				// 예외 처리 로직
				log.info("예외 처리, message={}", e.getMessage(), e);
			}
		}

		/**
		 * Checked Exception을 밖으로 던지는 코드
		 * throws 예외를 메서드에 필수로 선언해야 한다
		 * @throws MyCheckedException
		 */
		public void callThrow() throws MyCheckedException {
			repository.call();
		}

	}

	static class Repository {

		// catch()로 잡지 않으면 throws를 통해 던져야 한다
		public void call() throws MyCheckedException {
			throw new MyCheckedException("ex");
		}

	}

}
