package toy.order.domain.common.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import toy.order.domain.common.exception.UnauthorizedException;
import toy.order.domain.common.session.SessionConst;
import toy.order.domain.member.Member;
import toy.order.domain.member.MemberRepository;

import javax.security.auth.login.LoginException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CurrentMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(CurrentMember.class);
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        String loginId = (String) webRequest.getAttribute(SessionConst.LOGIN_MEMBER_ID, RequestAttributes.SCOPE_SESSION);

        if(loginId == null){
            throw new LoginException("User not logged in");
        }

        Optional<Member> member = memberRepository.findByLoginId(loginId);
        System.out.println("member.toString() = " + member.toString());
        
        // orElseThrow() 메서드로 Optional 내부의 Member 객체 꺼내 쓰기
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UnauthorizedException("User not logged in"));
    }
}