package toy.order.domain.member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    //CRUD
    Member save (Member member);
    Member findByMemberId(Long memberId);
    Member findByUuid(String uuid);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findAll();
    void update(String uuid, String name, String loginId, String password);
    void updateBalance(Long memberId, double amount);
    void delete(String loginId);
    Long findMemberIdByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByUuid(String uuid);

}
