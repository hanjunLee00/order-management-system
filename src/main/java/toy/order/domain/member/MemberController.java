package toy.order.domain.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member){
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        // UUID 생성 및 설정
        member.setUuid(UUID.randomUUID().toString());
        memberRepository.save(member);
        return "redirect:/";
    }

    @GetMapping("/{uuid}/edit")
    public String editForm(@PathVariable String uuid, Model model){
        Member member = memberRepository.findByUuid(uuid);
        model.addAttribute("member", member);
        return "members/editMemberForm";
    }

    @PostMapping("/{uuid}/edit")
    public String update(@PathVariable String uuid, @Valid @ModelAttribute Member member, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "members/editMemberForm";
        }
        memberRepository.update(uuid, member.getName(), member.getLoginId(), member.getPassword());
        return "loginHome";
    }
}
