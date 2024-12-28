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
    void delete(String loginId);
    Long findMemberIdByLoginId(String loginId);
    //아이디 중복 체크 로직
    boolean existsByLoginId(String loginId);

}
