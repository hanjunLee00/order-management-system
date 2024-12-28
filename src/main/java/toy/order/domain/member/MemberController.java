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
        // 로그인 ID 중복 검사
        if (memberRepository.existsByLoginId(member.getLoginId())) {
            bindingResult.rejectValue("loginId", "duplicate.member.loginId", "이미 존재하는 아이디입니다.");
            return "members/addMemberForm"; // 중복 시 에러 메시지와 함께 폼으로 돌아감
        }
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
        // 기존 회원 정보 조회
        Member existingMember = memberRepository.findByUuid(uuid);

        // 로그인 ID가 변경되었는지 확인
        if (!existingMember.getLoginId().equals(member.getLoginId())) {
            // 로그인 ID 중복 검사
            if (memberRepository.existsByLoginId(member.getLoginId())) {
                bindingResult.rejectValue("loginId", "duplicate.member.loginId", "이미 존재하는 아이디입니다.");
                return "members/editMemberForm"; // 중복 시 에러 메시지와 함께 폼으로 돌아감
            }
        }
        memberRepository.update(uuid, member.getName(), member.getLoginId(), member.getPassword());
        return "loginHome";
    }
}
