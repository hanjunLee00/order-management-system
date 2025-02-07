package toy.order.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toy.order.domain.member.Member;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByUuid(String uuid);

    Optional<Member> findByLoginId(String loginId);

    Long findMemberIdByLoginId(String loginId);

    @Query("SELECT m.balance FROM Member m WHERE m.uuid = :uuid")
    Integer findBalanceByUuid(@Param("uuid") String uuid);

    @Query("SELECT m.name FROM Member m WHERE m.uuid=:uuid")
    String findNameByUuid(String uuid);

    @Query("SELECT m.loginId FROM Member m WHERE m.uuid =:uuid")
    String findLoginIdByUuid(String uuid);

    Boolean existsByLoginId(String loginId);

    Boolean existsByUuid(String uuid);
}
