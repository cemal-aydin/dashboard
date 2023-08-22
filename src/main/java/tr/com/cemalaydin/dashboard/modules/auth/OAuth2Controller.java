//package tr.com.cemalaydin.dashboard.modules.auth;
//
//
//import tr.com.cemalaydin.dashboard.entities.User;
//import tr.com.cemalaydin.dashboard.helpers.AuthHelper;
//import tr.com.cemalaydin.dashboard.modules.user.dto.UserRegisterRequestDto;
//import tr.com.cemalaydin.dashboard.utils.JwtUtils;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//
//@RestController
//@RequestMapping("/oauth2")
//@RequiredArgsConstructor
//public class OAuth2Controller {
//
//    private final OAuth2AuthorizedClientService authorizedClientService;
//
//    @GetMapping("/loginSuccess")
//    public String loginSuccess(Object model, OAuth2AuthenticationToken authentication) {
//        OAuth2AuthorizedClient client = authorizedClientService
//                .loadAuthorizedClient(
//                        authentication.getAuthorizedClientRegistrationId(),
//                        authentication.getName());
//        //...
//        return "loginSuccess";
//    }
//
//    @GetMapping("/loginFailure")
//    public String loginFailure(Object model, OAuth2AuthenticationToken authentication) {
//        OAuth2AuthorizedClient client = authorizedClientService
//                .loadAuthorizedClient(
//                        authentication.getAuthorizedClientRegistrationId(),
//                        authentication.getName());
//        //...
//        return "loginFailure";
//    }
//
//}
