package toy.order.domain.member;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import toy.order.domain.common.session.SessionConst;
import toy.order.domain.member.dto.ChargeDto;
import toy.order.domain.member.dto.MemberUpdateDto;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;


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
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with UUID: " + uuid));
        model.addAttribute("member", member);
        return "members/editMemberForm";
    }

    @PostMapping("/{uuid}/edit")
    public String update(@PathVariable String uuid, @Valid @ModelAttribute("member") MemberUpdateDto updateDto,
                         BindingResult bindingResult, HttpSession session) {
        if(bindingResult.hasErrors()){
            return "members/editMemberForm";
        }
        // 기존 회원 정보 조회
        Member existingMember = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with UUID: " + uuid));

        // 로그인 ID가 변경되었는지 확인
        if (!existingMember.getLoginId().equals(updateDto.getLoginId())) {
            // 로그인 ID 중복 검사
            if (memberRepository.existsByLoginId(updateDto.getLoginId())) {
                bindingResult.rejectValue("loginId", "duplicate.member.loginId", "이미 존재하는 아이디입니다.");
                return "members/editMemberForm"; // 중복 시 에러 메시지와 함께 폼으로 돌아감
            }
        }
        memberRepository.update(uuid, updateDto.getName(), updateDto.getLoginId(), updateDto.getPassword());
        session.setAttribute(SessionConst.LOGIN_MEMBER_ID, memberRepository.findLoginIdByUuid(uuid));
        session.setAttribute(SessionConst.LOGIN_MEMBER_NAME, memberRepository.findNameByUuid(uuid));
        return "loginHome";
    }

    @GetMapping("/{uuid}/charge")
    public String chargeForm(@PathVariable String uuid, Model model){
        if(!memberRepository.existsByUuid(uuid)){
            return "redirect:/";
        }

        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with UUID: " + uuid));

        model.addAttribute("member", member);
        model.addAttribute("chargeDto", new ChargeDto());
        return "members/chargeForm";
    }

    @PostMapping("/{uuid}/charge")
    public String charge(@PathVariable String uuid,
                         @Valid @ModelAttribute ChargeDto chargeDto, BindingResult bindingResult,
                         Model model, HttpSession session){

        if(bindingResult.hasErrors()){
            model.addAttribute("member", memberRepository.findByUuid(uuid));
            return "members/chargeForm";
        }


        if(chargeDto.getAmount() < 1000){
            bindingResult.rejectValue("amount", "minAmount", "1000원 이상의 단위를 입력하세요");
            model.addAttribute("member", memberRepository.findByUuid(uuid));
            return "members/chargeForm";
        }

        //멤버를 uuid로 찾아서 충전 로직 수행
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Member not found with UUID: " + uuid));

        memberService.charge(member, chargeDto.getAmount());

        //헤더값 업데이트를 위해 세션값 업데이트
        session.setAttribute(SessionConst.LOGIN_MEMBER_BALANCE, memberRepository.findBalanceByUuid(uuid));

        //리다이렉트 페이지에서 값을 받아 사용하기 위해
        session.setAttribute("member", member);
        session.setAttribute("chargeDto", chargeDto);
        return "redirect:/members/" + uuid + "/charge/success";
    }

    @GetMapping("/{uuid}/charge/success")
    public String success(@PathVariable String uuid, HttpSession session, Model model){

        if(!memberRepository.existsByUuid(uuid)){
            return "redirect:/";
        }

        Member member = (Member) session.getAttribute("member");
        ChargeDto chargeDto = (ChargeDto) session.getAttribute("chargeDto");

        model.addAttribute("member", member);
        model.addAttribute("chargeDto", chargeDto);

        // 세션에서 데이터 제거 (PRG 패턴 완성)
        session.removeAttribute("member");
        session.removeAttribute("chargeDto");

        return "members/chargeSuccess";
    }
}
