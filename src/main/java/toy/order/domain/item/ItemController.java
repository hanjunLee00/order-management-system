package toy.order.domain.item;

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
import toy.order.domain.item.form.ItemPurchaseForm;
import toy.order.domain.item.form.ItemSaveForm;
import toy.order.domain.item.form.ItemUpdateForm;
import toy.order.domain.member.Member;
import toy.order.domain.member.MemberRepository;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final MemberRepository memberRepository;

    @GetMapping("/items")
    public String items(@CurrentMember Member loginMember, Model model) {
        List<Item> itemList = itemRepository.findAll();
        model.addAttribute("items", itemList);
        model.addAttribute("member", loginMember);
        return "items/items";
    }

    @GetMapping("/{itemId}")
    public String item(@CurrentMember Member loginMember,
                       @PathVariable Long itemId, Model model){
        Item item = itemRepository.findByItemId(itemId);
        model.addAttribute("item", item);
        model.addAttribute("member", loginMember);
        return "items/item";
    }

    @GetMapping("/add")
    public String addForm(@CurrentMember Member loginMember, Model model){
        Item newItem = new Item();
        model.addAttribute("item",newItem);
        model.addAttribute("member", loginMember);
        return "items/addForm";
    }

    @PostMapping("/add")
    public String addItem(@CurrentMember Member loginMember,
                          Model model,
                          @Validated @ModelAttribute("item") ItemSaveForm form,
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
        item.setMemberId(loginMember.getMemberId());

        Item savedItem = itemRepository.save(item);
        Long itemId = itemRepository.findItemIdByItemNameAndMemberId(savedItem.getItemName(), loginMember.getMemberId());
        redirectAttributes.addAttribute("itemId", itemId);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model){
        Item item = itemRepository.findByItemId(itemId);
        model.addAttribute("item", item);
        return "items/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit (
            @CurrentMember Member loginMember, Model model,
            @PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult){
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

        Item itemParam = itemRepository.findByItemId(itemId);
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());
        itemParam.setQuantity(form.getQuantity());

        itemRepository.update(itemId, itemParam);
        return "redirect:/items/{itemId}";
    }

    @GetMapping("/{uuid}/purchase/{itemId}")
    public String purchaseForm(@CurrentMember Member loginMember, Model model,
                               @PathVariable Long itemId, @PathVariable String uuid){
        if(memberRepository.findByUuid(uuid) == null){
            throw new IllegalArgumentException();
        }

        Item item = itemRepository.findByItemId(itemId);
        model.addAttribute("item", item);
        model.addAttribute("member", loginMember);
        return "items/purchaseForm";
    }

    @PostMapping("/{uuid}/purchase/{itemId}")
    public String purchase(@CurrentMember Member loginMember, Model model,
                           @PathVariable Long itemId, @PathVariable String uuid,
                           @Valid @ModelAttribute("item")ItemPurchaseForm form, BindingResult bindingResult){

        int resultPrice = 0;
        log.info("Entering purchase method with uuid={}, itemId={}, form={}", uuid, itemId, form);

        Item item = itemRepository.findByItemId(itemId);
        if (item == null){
            return "items/items";
        }

        log.info("Found item: {}", item);

        if (item.getPrice() !=null && item.getQuantity() !=null){
            resultPrice = item.getPrice()*form.getQuantity();
            System.out.println("resultPrice = " + resultPrice);
            if (form.getQuantity() > item.getQuantity())
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
        Long toId = itemRepository.findMemberIdByItemId(itemId);
        itemService.purchase(fromId, toId, resultPrice, item, form.getQuantity());
        model.addAttribute("member", loginMember);
        return "items/items";
    }

}
