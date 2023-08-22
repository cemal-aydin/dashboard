package tr.com.cemalaydin.dashboard.interceptor;

import tr.com.cemalaydin.dashboard.config.AppConfig;
import tr.com.cemalaydin.dashboard.entities.User;
import tr.com.cemalaydin.dashboard.modules.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {

    private final AppConfig appConfig;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object object) throws Exception {
        if (!appConfig.getSecurityEnabled()  ) {
            User u = userService.findByUsername("anonymousUser@localhost.com").orElse(null);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(u,
                    null, u.getAuthorities());
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(usernamePasswordAuthenticationToken);
            System.out.println("Request preHandle: " + request.getRequestURI());
        }
        return true;
    }
}
