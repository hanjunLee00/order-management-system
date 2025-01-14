package toy.order.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByUuid(String uuid);

    Optional<Member> findByLoginId(String loginId);

    Long findMemberIdByLoginId(String loginId);

    Integer findBalanceByUuid(String uuid);

    String findNameByUuid(String uuid);

    String findLoginIdByUuid(String uuid);

    Boolean existsByLoginId(String loginId);

    Boolean existsByUuid(String uuid);
}
