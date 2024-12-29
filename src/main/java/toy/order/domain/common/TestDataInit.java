package toy.order.domain.common;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toy.order.domain.item.Item;
import toy.order.domain.item.ItemRepositoryJdbc;
import toy.order.domain.member.Member;
import toy.order.domain.member.MemberRepositoryJdbc;

import java.util.UUID;

//@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final ItemRepositoryJdbc itemRepository;
    private final MemberRepositoryJdbc memberRepositoryJdbc;

    /**
     * 테스트용 데이터 추가
     * 멤버별 아이템 조회 가능하게
     */

//    @PostConstruct
    public void init() {
        Member memberA = new Member();
        Member memberB = new Member();

        Item itemA = new Item();
        Item itemB = new Item();
        Item itemC = new Item();
        Item itemD = new Item();

        createMemberA(memberA);
        createMemberB(memberB);

        Long memberAId = memberRepositoryJdbc.findMemberIdByLoginId("test1");
        Long memberBId = memberRepositoryJdbc.findMemberIdByLoginId("test2");

        System.out.println("memberAId = " + memberAId);
        System.out.println("memberBId = " + memberBId);

        createItemA(itemA, memberAId);
        createItemB(itemB, memberAId);
        createItemC(itemC, memberBId);
        createItemD(itemD, memberBId);
    }

    private void createItemA(Item itemA, Long memberAId) {
        itemA.setItemName("itemA");
        itemA.setPrice(10000);
        itemA.setQuantity(100);
        itemA.setMemberId(memberAId);
        itemRepository.save(itemA);
    }

    private void createItemB(Item itemB, Long memberAId) {
        itemB.setItemName("itemB");
        itemB.setPrice(20000);
        itemB.setQuantity(100);
        itemB.setMemberId(memberAId);
        itemRepository.save(itemB);
    }

    private void createItemC(Item itemC, Long memberBId) {
        itemC.setItemName("itemC");
        itemC.setPrice(30000);
        itemC.setQuantity(100);
        itemC.setMemberId(memberBId);
        itemRepository.save(itemC);
    }

    private void createItemD(Item itemD, Long memberBId) {
        itemD.setItemName("itemD");
        itemD.setPrice(40000);
        itemD.setQuantity(100);
        itemD.setMemberId(memberBId);
        itemRepository.save(itemD);
    }

    private void createMemberA(Member member) {

        member.setName("유저1");
        member.setLoginId("test1");
        member.setPassword("test");
        member.setUuid(UUID.randomUUID().toString());
        memberRepositoryJdbc.save(member);
    }

    private void createMemberB(Member member) {

        member.setName("유저2");
        member.setLoginId("test2");
        member.setPassword("test");
        member.setUuid(UUID.randomUUID().toString());
        memberRepositoryJdbc.save(member);
    }

    private String generateNewId (String basicId) {
        return basicId + "_" + System.currentTimeMillis();
    }
}
