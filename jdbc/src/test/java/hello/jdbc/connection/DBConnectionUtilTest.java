package hello.jdbc.connection;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.Test;

class DBConnectionUtilTest {

	@Test
	void connectionTest() {
		Connection connection = DBConnectionUtil.getConnection();

		assertThat(connection).isNotNull();
	}

}
