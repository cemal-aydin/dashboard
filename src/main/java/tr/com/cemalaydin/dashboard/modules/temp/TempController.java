package tr.com.cemalaydin.dashboard.modules.temp;

import tr.com.cemalaydin.dashboard.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temp")
public class TempController {

    @GetMapping("/temp")
    public String temp() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        System.out.println(((User)authentication.getPrincipal()).getEmail());
        return "OK";
    }
}
