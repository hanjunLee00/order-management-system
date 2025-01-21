package toy.order.domain.cart;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toy.order.domain.common.resolver.CurrentMember;
import toy.order.domain.item.Item;
import toy.order.domain.item.ItemRepository;
import toy.order.domain.item.ItemSearchCond;
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

    @PostMapping("/add/{itemId}") //items/items.html에서 btn을 통해 호출
    public String addCart(@CurrentMember Member loginMember, @PathVariable Long itemId,
                          @RequestParam Integer buyQuantity, HttpSession session, Model model,
                          RedirectAttributes redirectAttributes) {
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 아이템입니다: " + itemId));

        Optional<Cart> existItem = cartService.findByItemIdAndMemberId(itemId, loginMember.getMemberId());

        if (existItem.isEmpty()) {
            Cart cart = new Cart(buyQuantity, loginMember, item);
            cartService.create(cart);
        } else cartService.add(buyQuantity, itemId, loginMember.getMemberId());

        ItemSearchCond itemSearch = (ItemSearchCond) session.getAttribute("itemSearch");
        redirectAttributes.addFlashAttribute("itemSearch", itemSearch);

        return "redirect:/items/items";
    }

    @PostMapping("/update/{itemId}")
    public String updateCart(@CurrentMember Member loginMember, @PathVariable Long itemId, @RequestParam Integer quantity,
                             @RequestParam Long cartId){
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 아이템입니다." + itemId));

        Integer maxQuantity = cartService.findItemQuantityByCartId(cartId);

        if(maxQuantity < quantity){
            cartService.update(maxQuantity, loginMember.getMemberId(), item.getItemId());
        }
        cartService.update(quantity, itemId, loginMember.getMemberId());
        return "redirect:/cart";
    }

    @PostMapping("/delete/{itemId}")
    public String deleteCart(@CurrentMember Member loginMember, @PathVariable Long itemId, @RequestParam Long cartId) {
        itemRepository.findByItemId(itemId)
                .orElseThrow(()-> new IllegalArgumentException("유효하지 않은 아이템입니다." + itemId));

        cartService.delete(cartId);
        return "redirect:/cart";
    }
}
