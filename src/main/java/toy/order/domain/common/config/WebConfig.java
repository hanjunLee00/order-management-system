package toy.order.domain.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import toy.order.domain.common.interceptor.LoginCheckInterceptor;
import toy.order.domain.common.resolver.CurrentMemberArgumentResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers){
//        resolvers.add(new CurrentMemberArgumentResolver());
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "logout",
                        "/css/**", "/*.ico", "/error", "/400", "/500", "/api/**", "/api2/**");
    }
}
