package tr.com.cemalaydin.dashboard.modules.auth;

import tr.com.cemalaydin.dashboard.entities.User;
import tr.com.cemalaydin.dashboard.modules.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        UserDetails details = user.orElse(null) != null ? (UserDetails) user.get() : null;
        return details;
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        UserDetails details = user.orElse(null) != null ? (UserDetails) user.get() : null;
        return details;
    }
}
