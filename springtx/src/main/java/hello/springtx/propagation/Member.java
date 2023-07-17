package hello.springtx.propagation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

	@Id
	@GeneratedValue
	private Long id;
	private String username;

	/**
	 * 기본 생성자는 JPA 스펙 상 꼭 필요
	 */
	public Member() {
	}

	public Member(String username) {
		this.username = username;
	}

}
