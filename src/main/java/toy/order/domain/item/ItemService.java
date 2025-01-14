package toy.order.domain.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toy.order.domain.member.Member;
import toy.order.domain.member.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void purchase(Long fromId, Long toId, int money, Item item, Integer quantity){
        purchaseLogic(fromId, toId, money);
        itemDeductLogic(item, quantity);
    }

    // 상품을 구매하면 totalPrice를 계산해서 from에서 차감, to에서 증가
    private void purchaseLogic(Long fromId, Long toId, int money){

        Optional<Member> fromMember = memberRepository.findById(fromId);
        Optional<Member> toMember = memberRepository.findById(toId);

        memberRepository.updateBalance(fromId, fromMember.get().getBalance() - money);
        memberRepository.updateBalance(toId, toMember.get().getBalance() + money);
    }

    private void itemDeductLogic(Item item, Integer quantity){
        Integer updateQuantity = item.getQuantity() - quantity;
        itemRepository.updateCnt(item, updateQuantity);
    }

}
