package hello.jdbc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * SQLExceptionTranslator 추가
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository {

	private final DataSource dataSource;
	private final SQLExceptionTranslator exTranslator;

	public MemberRepositoryV4_2(DataSource dataSource) {
		this.dataSource = dataSource;
		// 변환기에 DataSource를 넣어 어떤 DB를 쓰는지 알려준다
		this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
	}

	@Override
	public Member save(Member member) {
		String sql = "insert into member(member_id, money) values (?, ?)";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setInt(2, member.getMoney());
			pstmt.executeUpdate(); // db row 개수를 반환
			return member;
		} catch (SQLException e) {
			throw exTranslator.translate("save", sql, e);
		} finally {
			close(con, pstmt, null);
		}
	}

	@Override
	public Member findById(String memberId) {
		String sql = "select * from member where member_id = ?";

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				Member member = new Member();
				member.setMemberId(rs.getString("member_Id"));
				member.setMoney(rs.getInt("money"));
				return member;
			} else {
				throw new NoSuchElementException("member not found memberId=" + memberId);
			}

		} catch (SQLException e) {
			throw exTranslator.translate("findById", sql, e);
		} finally {
			close(con, pstmt, rs);
		}
	}

	@Override
	public void update(String memberId, int money) {
		String sql = "update member set money=? where member_id=?";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, money);
			pstmt.setString(2, memberId);
			int resultSize = pstmt.executeUpdate();// db row 개수를 반환
			log.info("resultSize={}", resultSize);
		} catch (SQLException e) {
			throw exTranslator.translate("update", sql, e);
		} finally {
			close(con, pstmt, null);
		}
	}

	@Override
	public void delete(String memberId) {
		String sql = "delete from member where member_id=?";

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.executeUpdate();// db row 개수를 반환
		} catch (SQLException e) {
			throw exTranslator.translate("delete", sql, e);
		} finally {
			close(con, pstmt, null);
		}
	}

	private void close(Connection con, Statement stmt, ResultSet rs) {
		JdbcUtils.closeResultSet(rs);
		JdbcUtils.closeStatement(stmt);
		// 주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils 사용해야 함
		DataSourceUtils.releaseConnection(con, dataSource);
	}

	private Connection getConnection() throws SQLException {
		// 주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils 사용해야 함
		Connection con = DataSourceUtils.getConnection(dataSource);
		log.info("get connection={}, class={}", con, con.getClass());
		return con;
	}

}
