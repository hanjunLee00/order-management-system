package toy.order.domain.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "members/addMemberForm";
        }
        memberRepository.save(member);
        return "redirect:/";
    }

    @GetMapping("/{memberId}/edit")
    public String editForm(@PathVariable Long memberId, Model model){
        Member member = memberRepository.findByMemberId(memberId);
        model.addAttribute("member", member);
        return "members/editMemberForm";
    }

    //submit 하고 Post 요청 시 에러페이지 나오는 문제
    @PostMapping("/{memberId}/edit")
    public String update(@PathVariable Long memberId, @Valid @ModelAttribute Member member, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "members/editMemberForm";
        }
        memberRepository.update(memberId, member.getName(), member.getLoginId(), member.getPassword());
        return "loginHome";
    }
}
