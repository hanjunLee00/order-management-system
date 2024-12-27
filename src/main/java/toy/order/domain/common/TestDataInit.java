package toy.order.domain.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toy.order.domain.item.Item;
import toy.order.domain.item.ItemRepository;
import toy.order.domain.member.Member;
import toy.order.domain.member.MemberRepository;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    /**
     * 테스트용 데이터 추가
     */

    @PostConstruct
    public void init() {

        Member member = new Member();
        String basicId = "test";
        String newId = generateNewId(basicId);
        member.setLoginId(newId);
        member.setPassword("test");
        member.setName("테스터");

        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getMemberId();

        itemRepository.save(new Item("itemA", 10000, 10, memberId));
        itemRepository.save(new Item("itemB", 20000, 20, memberId));

    }

    private String generateNewId (String basicId) {
        return basicId + "_" + System.currentTimeMillis();
    }
}
