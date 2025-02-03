package toy.order.domain.member.repository;

import toy.order.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    //CRUD
    Member save (Member member);
    Optional<Member> findById(Long memberId);
    Optional<Member> findByUuid(String uuid);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findAll();
    void update(String uuid, String name, String loginId, String password);
    void updateBalance(Long memberId, int amount);
    void delete(String loginId);
    Long findMemberIdByLoginId(String loginId);
    Integer findBalanceByUuid(String uuid);
    String findNameByUuid(String uuid);
    String findLoginIdByUuid(String uuid);
    boolean existsByLoginId(String loginId);
    boolean existsByUuid(String uuid);
}
