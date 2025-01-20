package toy.order.domain.item;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toy.order.domain.common.resolver.CurrentMember;
import toy.order.domain.common.session.SessionConst;
import toy.order.domain.item.dto.ItemPurchaseDto;
import toy.order.domain.item.dto.ItemSaveDto;
import toy.order.domain.item.dto.ItemUpdateDto;
import toy.order.domain.member.Member;
import toy.order.domain.member.MemberRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final MemberRepository memberRepository;

    @GetMapping("/items")
    public String items(@CurrentMember Member loginMember, @ModelAttribute("itemSearch") ItemSearchCond itemSearch,
                        Model model, HttpSession session) {
        List<Item> itemList = itemRepository.findItems(itemSearch);
        model.addAttribute("items", itemList);
        model.addAttribute("member", loginMember);
        session.setAttribute("itemSearch", itemSearch);
        return "items/items";
    }

    @GetMapping("/{itemId}")
    public String item(@CurrentMember Member loginMember,
                       @PathVariable Long itemId, Model model){
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

        model.addAttribute("item", item);
        model.addAttribute("member", loginMember);
        return "items/item";
    }

    @GetMapping("/add")
    public String addForm(@CurrentMember Member loginMember, Model model){
        Item newItem = new Item();
        model.addAttribute("item", newItem);
        model.addAttribute("member", loginMember);
        return "items/addForm";
    }

    @PostMapping("/add")
    public String addItem(@CurrentMember Member loginMember,
                          Model model,
                          @Validated @ModelAttribute("item") ItemSaveDto form,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (form.getPrice() != null && form.getQuantity() != null) {
            double resultPrice = form.getPrice()*form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "items/addForm";
        }
        model.addAttribute("member", loginMember);

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());
        item.setMember(loginMember);

        Item savedItem = itemRepository.save(item);
        Long itemId = itemRepository.findItemIdByItemNameAndMember(savedItem.getItemName(), loginMember);
        redirectAttributes.addAttribute("itemId", itemId);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@CurrentMember Member loginMember, @PathVariable Long itemId, Model model){
        if(loginMember == null){
            return "/";
        }
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));
        model.addAttribute("item", item);
        return "items/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit (
            @CurrentMember Member loginMember, Model model,
            @PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateDto form, BindingResult bindingResult){
        if(form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice()*form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "items/editForm";
        }

        model.addAttribute("member", loginMember);
        itemRepository.update(itemId, form);
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{uuid}/purchase/{itemId}")
    public String purchaseForm(@CurrentMember Member loginMember, Model model,
                               @PathVariable Long itemId, @PathVariable String uuid,
                               @RequestParam("buyQuantity") int buyQuantity,
                               RedirectAttributes redirectAttributes){
        if(loginMember == null){
            redirectAttributes.addFlashAttribute("loginMessage", "로그인 후 구매할 수 있습니다.");
            return "redirect:/items/items";
        }

        if(memberRepository.findByUuid(uuid).isEmpty()){
            throw new IllegalArgumentException();
        }

        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

        ItemPurchaseDto purchaseDto = new ItemPurchaseDto();
        purchaseDto.setQuantity(buyQuantity);

        model.addAttribute("item", item);
        model.addAttribute("dto", purchaseDto);
        model.addAttribute("member", loginMember);
        return "items/purchaseForm";
    }

    @PostMapping("/{uuid}/purchase/{itemId}")
    public String purchase(@CurrentMember Member loginMember, Model model,
                           @PathVariable Long itemId, @PathVariable String uuid,
                           @Valid @ModelAttribute("item") ItemPurchaseDto dto, BindingResult bindingResult,
                           HttpSession session){

        int resultPrice = 0;
        log.info("Entering purchase method with uuid={}, itemId={}, form={}", uuid, itemId, dto);

        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

        log.info("Found item: {}", item);

        if (item.getPrice() !=null && item.getQuantity() !=null){
            resultPrice = item.getPrice()*dto.getQuantity();
            System.out.println("resultPrice = " + resultPrice);
            if (dto.getQuantity() > item.getQuantity())
                bindingResult.rejectValue("quantity", "outOfStock", "재고 부족");
            if (resultPrice > loginMember.getBalance())
                bindingResult.rejectValue("quantity", "insufficientBalance", "잔액 부족");
        }

        if (bindingResult.hasErrors()){
            System.out.println("bindingResult = " + bindingResult);
            model.addAttribute("member", loginMember);
            model.addAttribute("item", item);
            return "items/purchaseForm";
        }

        Long fromId = loginMember.getMemberId();
        Member toMember = itemRepository.findMemberByItemId(itemId);
        Long toId = toMember.getMemberId();
        itemService.purchase(fromId, toId, resultPrice, item, dto.getQuantity());

        session.setAttribute(SessionConst.LOGIN_MEMBER_BALANCE, memberRepository.findBalanceByUuid(uuid));

        Integer itemPrice = itemRepository.findPriceByItemId(itemId);
        String itemName = itemRepository.findItemNameByItemId(itemId);

        session.setAttribute("purchaseCnt", dto.getQuantity());
        session.setAttribute("itemPrice", itemPrice);
        session.setAttribute("itemId", itemId);
        session.setAttribute("itemName", itemName);

//        ItemSearchCond itemSearchCond = (ItemSearchCond) session.getAttribute("itemSearch");
//        if (itemSearchCond != null) {
//            redirectAttributes.addAttribute("itemName", itemSearchCond.getItemName());
//            redirectAttributes.addAttribute("maxPrice", itemSearchCond.getMaxPrice());
//            // 필요한 다른 필드도 추가
//        }

        return "redirect:/items/" + uuid + "/purchase/success";
    }

    @GetMapping("{uuid}/purchase/success")
    public String purchaseSuccess(@PathVariable String uuid, HttpSession session, Model model) {
        if (!memberRepository.existsByUuid(uuid)) {
            return "redirect:/";
        }
        /**
         * 충전완료 폼에서 보여주어야 할 것들
         * 1. 결제금액 = 상품가격 item.getPrice() * 구매수량 item.getQuantity()
         * 2. 사용자 잔액 = member.getBalance();
         * 목록으로 / 추가주문
         */

        Integer purchaseCnt = (Integer) session.getAttribute("purchaseCnt");
        Integer itemPrice = (Integer) session.getAttribute("itemPrice");
        Long itemId = (Long) session.getAttribute("itemId");
        String itemName = (String) session.getAttribute("itemName");

        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + uuid));
        ItemSearchCond itemSearch = (ItemSearchCond) session.getAttribute("itemSearch");

        model.addAttribute("itemName", itemName);
        model.addAttribute("member", member);
        model.addAttribute("itemSearch", itemSearch);
        model.addAttribute("purchaseCnt", purchaseCnt);
        model.addAttribute("itemPrice", itemPrice);
        model.addAttribute("itemId", itemId);

        return "items/purchaseSuccess";
    }

}
