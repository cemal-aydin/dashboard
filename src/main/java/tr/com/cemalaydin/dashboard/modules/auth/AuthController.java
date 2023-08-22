package tr.com.cemalaydin.dashboard.modules.auth;


import tr.com.cemalaydin.dashboard.base.DataResult;
import tr.com.cemalaydin.dashboard.entities.User;
import tr.com.cemalaydin.dashboard.helpers.JedisHelper;
import tr.com.cemalaydin.dashboard.modules.user.dto.ForgotPasswordRequestDto;
import tr.com.cemalaydin.dashboard.modules.user.dto.UserDto;
import tr.com.cemalaydin.dashboard.modules.user.dto.UserRegisterRequestDto;
import tr.com.cemalaydin.dashboard.modules.user.UserService;
import tr.com.cemalaydin.dashboard.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JpaUserDetailsService jpaUserDetailsService;

    private final UserService userService;

    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody LoginRequestDto request, HttpServletResponse response) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword(),
                            new ArrayList<>()));
            final UserDetails user = jpaUserDetailsService.loadUserByUsername(request.getUsername());
            if (!user.isEnabled()) {
                return ResponseEntity.status(400).body(new DataResult("", "User disabled"));
            }
            if (user != null) {
                String token = jwtUtils.generateToken(user);
                String renewToken = jwtUtils.generateRenewToken(user);
                JedisHelper.jedisSadd(JedisHelper.TOKEN_KEY, token);
                JedisHelper.jedisSadd(JedisHelper.RENEW_TOKEN_KEY, renewToken);
                Map<String, Object> res = new HashMap<>();
                res.put("jwt", token);
                res.put("refreshJwt", renewToken);
                res.put("username",request.getUsername());
                res.put("userId", ((User)user).getId());
                res.put("userFullName", ((User)user).getFullName());
                res.put("emailValidated", ((User)user).getEmailValidated());
                res.put("provider", ((User)user).getProvider());
                return ResponseEntity.ok(new DataResult(res));
            }
            return ResponseEntity.status(400).body(new DataResult("","Error authenticating"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(400).body(new DataResult("",e.getMessage()));
        }
    }

    @PostMapping("/signinWithToken")
    public ResponseEntity signinWithToken(@RequestBody LoginRequestDto request, HttpServletResponse response) {
        try {
            if (!JedisHelper.jedisSmembersExists(JedisHelper.RENEW_TOKEN_KEY, request.getRefreshJwt())) {
                return ResponseEntity.status(400).body(new DataResult("", "Renew token invalid."));
            }
            final UserDetails user = jpaUserDetailsService.loadUserByUsername(request.getUsername());
            jwtUtils.validateToken(request.getRefreshJwt(),user);
            if (!user.isEnabled()) {
                return ResponseEntity.status(400).body(new DataResult("", "User disabled"));
            }
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getUsername(), null,
                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);

            if (user != null) {
                String token = jwtUtils.generateToken(user);
                String renewToken = jwtUtils.generateRenewToken(user);
                JedisHelper.jedisSadd(JedisHelper.TOKEN_KEY, token);
                JedisHelper.jedisSadd(JedisHelper.RENEW_TOKEN_KEY, renewToken);
                Map<String, Object> res = new HashMap<>();
                res.put("jwt", token);
                res.put("refreshJwt", renewToken);
                res.put("username",request.getUsername());
                res.put("userId", ((User)user).getId());
                res.put("userFullName", ((User)user).getFullName());
                res.put("emailValidated", ((User)user).getEmailValidated());
                res.put("provider", ((User)user).getProvider());
                return ResponseEntity.ok(new DataResult(res));
            }
            return ResponseEntity.status(400).body(new DataResult("","Error authenticating"));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(400).body(new DataResult("",e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRegisterRequestDto user) throws Exception {
        try {
            User userResponse = userService.registerUser(user.toEntity());
            UserDto userDto = UserDto.builder()
                    .pictureUrl(userResponse.getPictureUrl())
                    .emailValidated(userResponse.getEmailValidated())
                    .email(userResponse.getEmail())
                    .username(userResponse.getUsername())
                    .fullName(userResponse.getFullName())
                    .id(userResponse.getId())
                    .provider(userResponse.getProvider())
                    .allowSendValidationEmailAgain(DateUtils.addMinutes(userResponse.getEmailValidationCodeSentDate(), 10).before(new Date()))
                    .build();

            return ResponseEntity.ok(new DataResult(userDto));
        } catch (Exception e) {
            if (e.getMessage().equals("usernameUsed")) {
            return ResponseEntity.status(400).body(new DataResult("400-101","Kullanıcı adı daha önceden kullanılmış"));
            } else if (e.getMessage().equals("emailUsed")) {
                return ResponseEntity.status(400).body(new DataResult("400-102","E-posta adresi daha önceden kullanılmış"));
            } else {
                throw  e;
            }
        }
    }

    @GetMapping("/signout")
    public ResponseEntity logout(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken = null;
        jwtToken = authHeader.substring(7);
        JedisHelper.jedisSrem(JedisHelper.TOKEN_KEY, jwtToken);
        //AuthHelper.ACTIVE_JWT_LIST.remove(jwtToken);
        return ResponseEntity.ok(new DataResult("Success"));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) throws Exception {
        try {
            return ResponseEntity.ok(new DataResult(userService.forgotPassword(forgotPasswordRequestDto)));
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new DataResult("",e.getMessage()), HttpStatusCode.valueOf(400));
        }
    }

    @PostMapping("/resetForgottenPassword")
    public ResponseEntity resetForgottenPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) throws Exception {
        try {
            userService.resetForgottenPassword(forgotPasswordRequestDto);
            return ResponseEntity.ok(new DataResult());
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new DataResult("",e.getMessage()), HttpStatusCode.valueOf(400));
        }
    }


}
