package tr.com.cemalaydin.dashboard.helpers;

import tr.com.cemalaydin.dashboard.base.BaseClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class AuthHelper extends BaseClass {

//    public static final List<String> ACTIVE_JWT_LIST = new ArrayList<>();

    public static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
      //      "/swagger-ui.html",
      //      "/swagger-ui/**",
            "/oauth2/**",
            "/auth/**"
    };

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
