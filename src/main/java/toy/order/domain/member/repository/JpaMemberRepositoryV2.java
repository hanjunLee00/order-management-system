package toy.order.domain.member.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import toy.order.domain.member.Member;

import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
@RequiredArgsConstructor
public class JpaMemberRepositoryV2 implements MemberRepository {

    private final SpringDataJpaMemberRepository repository;

    @Override
    public Member save(Member member) {
        return repository.save(member);
    }

    public Optional<Member> findById(Long memberId) {
        return repository.findById(memberId);
    }

    @Override
    public Optional<Member> findByUuid(String uuid) {
        return repository.findByUuid(uuid);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return repository.findByLoginId(loginId);
    }

    @Override
    public List<Member> findAll() {
        return repository.findAll();
    }

    @Override
    public void update(String uuid, String name, String loginId, String password) {
        Optional<Member> findMember = findByUuid(uuid);
        findMember.get().setName(name);
        findMember.get().setLoginId(loginId);
        findMember.get().setPassword(password);
    }

    @Override
    public void updateBalance(Long memberId, int amount) {
        Optional<Member> findMember = findById(memberId);
        findMember.get().setBalance(amount);
    }

    @Override
    public void delete(String loginId) {
        Optional<Member> findMember = repository.findByLoginId(loginId);
        findMember.ifPresent(repository::delete);
    }

    @Override
    public Long findMemberIdByLoginId(String loginId) {
        return repository.findMemberIdByLoginId(loginId);
    }

    @Override
    public Integer findBalanceByUuid(String uuid) {
        return repository.findBalanceByUuid(uuid);
    }

    @Override
    public String findNameByUuid(String uuid) {
        return repository.findNameByUuid(uuid);
    }

    @Override
    public String findLoginIdByUuid(String uuid) {
        return repository.findLoginIdByUuid(uuid);
    }

    @Override
    public boolean existsByLoginId(String loginId) {
        return repository.existsByLoginId(loginId);
    }

    @Override
    public boolean existsByUuid(String uuid) {
        return repository.existsByUuid(uuid);
    }
}
