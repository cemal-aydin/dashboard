package tr.com.cemalaydin.dashboard.config;

import tr.com.cemalaydin.dashboard.base.DataResult;
import tr.com.cemalaydin.dashboard.helpers.AuthHelper;
import tr.com.cemalaydin.dashboard.helpers.JedisHelper;
import tr.com.cemalaydin.dashboard.modules.auth.JpaUserDetailsService;
import tr.com.cemalaydin.dashboard.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JpaUserDetailsService jpaUserDetailsService;
    private final JwtUtils jwtUtils;
    private final AppConfig appConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String userEmail;
        String jwtToken = null;

//        if (!appConfig.getSecurityEnabled()) {
//            //filterChain.doFilter(request, response);
//            return;
//        }



//        for (Cookie cookie : request.getCookies()) {
//            if (cookie.getName().equals("jwt")) {
//                jwtToken = cookie.getValue();
////                System.out.println(cookie.getValue());
//            }
//        }
//        if (jwtToken == null) {
//            filterChain.doFilter(request, response);
//            return;
//        }


        if (appConfig.getSecurityEnabled()) {

            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwtToken = StringUtils.isBlank(authHeader) ? null : authHeader.substring(7);


//            if (AuthHelper.ACTIVE_JWT_LIST.contains(jwtToken)) {
            if (JedisHelper.jedisSmembersExists(JedisHelper.TOKEN_KEY, jwtToken)) {
                userEmail = jwtUtils.extractUsername(jwtToken);
                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(userEmail);
                    if (jwtUtils.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } else {
                response.setStatus(401);
                response.setContentType("application/json");

                //pass down the actual obj that exception handler normally send
                ObjectMapper mapper = new ObjectMapper();
                PrintWriter out = response.getWriter();
                //out.print(new ModelMapper().map(new DataResult("","401 hata detayı"), String.class));
                out.flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.asList(AuthHelper.AUTH_WHITELIST).stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
    }
}

