package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    // findByName()을 호출하면 JPQL 쿼리가 생성됨
    // "select m from Member m where m.name = ?"
    @Override
    Optional<Member> findByName(String name);
}
