package hello.jdbc.exception.basic;

import static org.assertj.core.api.Assertions.*;

import java.net.ConnectException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class CheckedAppTest {

	@Test
	void checked() {
		Controller controller = new Controller();
		assertThatThrownBy(controller::request)
			.isInstanceOf(Exception.class); // Exception은 Checked Exception
	}

	static class Controller {

		Service service = new Service();

		public void request() throws SQLException, ConnectException {
			service.logic();
		}

	}

	static class Service {

		Repository repository = new Repository();
		NetworkClient networkClient = new NetworkClient();

		public void logic() throws ConnectException, SQLException {
			repository.call();
			networkClient.call();
		}

	}

	static class NetworkClient {

		public void call() throws ConnectException {
			throw new ConnectException("연결 실패");
		}

	}

	static class Repository {

		public void call() throws SQLException {
			throw new SQLException("ex");
		}

	}

}
