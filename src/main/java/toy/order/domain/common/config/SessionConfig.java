package toy.order.domain.common.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class SessionConfig {

    @Bean
    @SessionScope
    public HttpSession httpSession(HttpServletRequest request) {return request.getSession();}
}
