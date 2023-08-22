package tr.com.cemalaydin.dashboard.config;

import tr.com.cemalaydin.dashboard.entities.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware {

    @Override
    public Optional<String> getCurrentAuditor() {
        String username = "init";
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String) {
                username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            } else  {
                username = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            }
        }
        return Optional.of(username);
    }
}
