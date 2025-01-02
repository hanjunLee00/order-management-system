package toy.order.domain.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toy.order.domain.member.Member;
import toy.order.domain.member.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void purchase(Long fromId, Long toId, double money, Item item, Integer quantity){
        purchaseLogic(fromId, toId, money);
        itemDeductLogic(item, quantity);
    }

    // 상품을 구매하면 totalPrice를 계산해서 from에서 차감, to에서 증가
    private void purchaseLogic(Long fromId, Long toId, double money){

        Member fromMember = memberRepository.findByMemberId(fromId);
        Member toMember = memberRepository.findByMemberId(toId);

        memberRepository.updateBalance(fromId, fromMember.getBalance() - money);
        memberRepository.updateBalance(toId, toMember.getBalance() + money);
    }

    private void itemDeductLogic(Item item, Integer quantity){
        Integer updateQuantity = item.getQuantity() - quantity;
        itemRepository.updateCnt(item, updateQuantity);
    }

}
