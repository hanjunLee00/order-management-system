package toy.order.domain.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import toy.order.domain.common.session.SessionConst;
import toy.order.domain.item.form.ItemSaveForm;
import toy.order.domain.item.form.ItemUpdateForm;
import toy.order.domain.member.Member;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/uuid/{uuid}")
    public String items(@SessionAttribute(name= SessionConst.LOGIN_MEMBER, required=false) Member loginMember,
                        @PathVariable String uuid, Model model) {
        List<Item> itemList = itemRepository.findByUuid(uuid);
        model.addAttribute("items", itemList);
        model.addAttribute("member", loginMember);
        return "items/items";
    }

    @GetMapping("/{itemId}")
    public String item(@SessionAttribute(name= SessionConst.LOGIN_MEMBER, required=false) Member loginMember,
                       @PathVariable Long itemId, Model model){
        Item item = itemRepository.findByItemId(itemId);
        model.addAttribute("item", item);
        model.addAttribute("member", loginMember);
        return "items/item";
    }

    @GetMapping("/add")
    public String addForm(@SessionAttribute(name= SessionConst.LOGIN_MEMBER, required=false) Member loginMember, Model model){
        Item newItem = new Item();
        model.addAttribute("item",newItem);
        model.addAttribute("member", loginMember);
        return "items/addForm";
    }

    @PostMapping("/add")
    public String addItem(@SessionAttribute(name= SessionConst.LOGIN_MEMBER, required=false) Member loginMember,
                          Model model,
                          @Validated @ModelAttribute("item") ItemSaveForm form,
                          BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice()*form.getQuantity();
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
            @SessionAttribute(name= SessionConst.LOGIN_MEMBER, required=false) Member loginMember, Model model,
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

}
