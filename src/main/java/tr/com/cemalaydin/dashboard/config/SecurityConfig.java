package tr.com.cemalaydin.dashboard.config;


import tr.com.cemalaydin.dashboard.entities.User;
import tr.com.cemalaydin.dashboard.enums.AuthProvider;
import tr.com.cemalaydin.dashboard.enums.Status;
import tr.com.cemalaydin.dashboard.helpers.AuthHelper;
import tr.com.cemalaydin.dashboard.modules.auth.JpaUserDetailsService;
import tr.com.cemalaydin.dashboard.modules.user.UserService;
import tr.com.cemalaydin.dashboard.utils.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final JpaUserDetailsService jpaUserDetailsService;
    private final UserService userService;
    private final AppConfig appConfig;
    private final JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String securityDisabledFilter = appConfig.getSecurityEnabled() ? "": "/**";
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(securityDisabledFilter).permitAll()
                        .requestMatchers(AuthHelper.AUTH_WHITELIST).permitAll()
                      //  .requestMatchers("/temp/**").hasRole("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorizeClient")
                .and()
                //.defaultSuccessUrl("/oauth2/loginSuccess")
                .successHandler(authSuccessHandler())
                .failureUrl("/oauth2/loginFailure")
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(jpaUserDetailsService)
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                Map<String, Object> attributes = ((DefaultOAuth2User) authentication.getPrincipal()).getAttributes();
                UserDetails userDetails = jpaUserDetailsService.loadUserByEmail(attributes.get("email").toString());
                if (userDetails == null) {
                    User u = new User();
                    u.setFullName(attributes.get("name").toString());
                    u.setStatus(Status.ACTIVE);
                    u.setRoles("ROLE_USER");
                    u.setEmail(attributes.get("email").toString());
                    u.setUsername(attributes.get("email").toString());
                    u.setProviderId(attributes.get("sub").toString());
                    u.setPictureUrl(attributes.get("picture").toString());
                    u.setLocale(attributes.get("locale").toString());
                    u.setProvider(AuthProvider.GOOGLE);
                    userDetails = (UserDetails) userService.save(u);
                }
                if (userDetails != null) {
                    String jwt = jwtUtils.generateToken(userDetails);
                    response.sendRedirect("http://localhost:3000?jwt=" + jwt);
                }

            }
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
