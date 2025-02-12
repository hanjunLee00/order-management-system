package toy.order.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.order.domain.member.Member;
import toy.order.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m->m.getPassword().equals(password))
                .orElse(null);
    }
}