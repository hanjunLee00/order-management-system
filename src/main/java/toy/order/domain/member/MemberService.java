package toy.order.domain.member;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void charge(Member member, Integer amount){
        chargeLogic(member, amount);
    }

    private void chargeLogic(Member member, Integer amount){
        int updatedAmount = member.getBalance() + amount;
        memberRepository.updateBalance(member.getMemberId(), updatedAmount);
    }
}
