package toy.order.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByUuid(String uuid);

    Optional<Member> findByLoginId(String loginId);

    Long findMemberIdByLoginId(String loginId);

    @Query("SELECT m.balance FROM Member m WHERE m.uuid = :uuid")
    Integer findBalanceByUuid(@Param("uuid") String uuid);

    String findNameByUuid(String uuid);

    String findLoginIdByUuid(String uuid);

    Boolean existsByLoginId(String loginId);

    Boolean existsByUuid(String uuid);
}
