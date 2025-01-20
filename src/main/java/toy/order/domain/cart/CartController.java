package toy.order.domain.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import toy.order.domain.common.resolver.CurrentMember;
import toy.order.domain.item.Item;
import toy.order.domain.item.ItemRepository;
import toy.order.domain.member.Member;
import toy.order.domain.member.MemberRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("cart")
public class CartController {

    private final CartService cartService;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @GetMapping
    public String cart(@CurrentMember Member loginMember, Model model) {
        List<Cart> list = cartService.findByMemberId(loginMember.getMemberId());
        log.info("loginMember.getMemberId() = {}", loginMember.getMemberId());
        model.addAttribute("member", loginMember);
        model.addAttribute("cartList", list);
        return "cart/cart";
    }

    @PostMapping("/add") //items/items.html에서 btn을 통해 호출
    public String addCart(@CurrentMember Member loginMember, @RequestParam Long itemId) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 아이템입니다: " + itemId));
        /**
         * 장바구니에 같은 아이템이 없으면 create
         * create 하려면 = quantity, member(memberId), item(itemId) 필요함
         * 장바구니에 같은 아이템이 있으면 update (개수 추가)
         */

//        if (cartService.findByItemId(itemId).isEmpty()) {
//            item.setQuantity(item.getQuantity() + );
//            cartService.add(item);
//        }

        return "cart";
    }
}
