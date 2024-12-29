package toy.order.domain.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import toy.order.domain.common.resolver.CurrentMember;
import toy.order.domain.common.session.SessionConst;
import toy.order.domain.member.Member;
import toy.order.domain.member.MemberRepositoryJdbc;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepositoryJdbc memberRepositoryJdbc;

//    @GetMapping("/")
//    public String homeLogin(@SessionAttribute(name= SessionConst.LOGIN_MEMBER, required=false) Member loginMember, Model model) {
//        //세션에 데이터가 없으면 home
//        if (loginMember == null) {
//            return "home";
//        }
//
//        //세션이 유지되면 로그인으로 이동
//        model.addAttribute("member", loginMember);
//        return "loginHome";
//    }



    @GetMapping("/")
    public String homeLoginArgumentResolver(@CurrentMember Member loginMember, Model model) {
        if(loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
