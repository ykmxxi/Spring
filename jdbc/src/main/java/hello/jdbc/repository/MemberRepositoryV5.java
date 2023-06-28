package hello.jdbc.repository;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

/**
 * JdbcTemplate 사용
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository {

	// 커넥션 동기화, 닫기 등을 JdbcTemplate이 모두 해줌
	private final JdbcTemplate template;

	public MemberRepositoryV5(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public Member save(Member member) {
		String sql = "insert into member(member_id, money) values (?, ?)";
		template.update(sql, member.getMemberId(), member.getMoney());
		return member;
	}

	@Override
	public Member findById(String memberId) {
		String sql = "select * from member where member_id = ?";
		// 단건 조회
		return template.queryForObject(sql, memberRowMapper(), memberId);
	}

	private RowMapper<Member> memberRowMapper() {
		return (rs, rowNum) -> {
			Member member = new Member();
			member.setMemberId(rs.getString("member_id"));
			member.setMoney(rs.getInt("money"));
			return member;
		};
	}

	@Override
	public void update(String memberId, int money) {
		String sql = "update member set money=? where member_id=?";
		template.update(sql, money, memberId);
	}

	@Override
	public void delete(String memberId) {
		String sql = "delete from member where member_id=?";
		template.update(sql, memberId);
	}

}
