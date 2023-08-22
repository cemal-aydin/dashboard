package tr.com.cemalaydin.dashboard;


import tr.com.cemalaydin.dashboard.entities.User;
import tr.com.cemalaydin.dashboard.enums.AuthProvider;
import tr.com.cemalaydin.dashboard.enums.Status;
import tr.com.cemalaydin.dashboard.modules.user.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppInit {

    private final UserService userService;

    @PostConstruct
    private void startup() {
        addAdminUser();
        addAnonymousUser();
    }

    private void addAdminUser() {
        String adminUsername = "admin@localhost.com";
        User u = userService.findByUsername(adminUsername).orElse(null);
        if (u == null) {
            u = new User();
            u.setEmail(adminUsername);
            u.setUsername(adminUsername);
            u.setFullName("Admin Admin");
            u.setRoles("ROLE_ADMIN");
            u.setStatus(Status.ACTIVE);
            u.setPassword(new BCryptPasswordEncoder().encode("admin"));
            u.setProvider(AuthProvider.LOCAL);
            userService.save(u);
            System.out.println("Admin user added.");
        } else {
            System.out.println("Admin user already exits.");
        }
    }

    private void addAnonymousUser() {
        String adminUsername = "anonymousUser@localhost.com";
        User u = userService.findByUsername(adminUsername).orElse(null);
        if (u == null) {
            u = User.builder()
                    .email(adminUsername)
                    .username(adminUsername)
                    .fullName("anonymousUser")
                    .roles("ROLE_USER")
                    .password(new BCryptPasswordEncoder().encode("admin"))
                    .provider(AuthProvider.LOCAL)
                    .build();
            u.setStatus(Status.ACTIVE);

//            u = new User();
//            u.setEmail(adminUsername);
//            u.setUsername(adminUsername);
//            u.setFullName("anonymousUser");
//            u.setRoles("ROLE_USER");
//            u.setStatus(Status.ACTIVE);
//            u.setPassword(new BCryptPasswordEncoder().encode("admin"));
//            u.setProvider(AuthProvider.LOCAL);
            userService.save(u);
            System.out.println("anonymousUser user added.");
        } else {
            System.out.println("anonymousUser user already exits.");
        }
    }
}
