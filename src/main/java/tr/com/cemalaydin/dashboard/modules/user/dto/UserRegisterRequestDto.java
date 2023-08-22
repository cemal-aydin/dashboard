package tr.com.cemalaydin.dashboard.modules.user.dto;

import tr.com.cemalaydin.dashboard.entities.User;
import tr.com.cemalaydin.dashboard.enums.AuthProvider;
import tr.com.cemalaydin.dashboard.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterRequestDto {
    private String fullName;
    @NotNull
    private String username;
    private String email;
    private String phone;
    private String password;
    private String roles;
    @NotNull
    private AuthProvider provider;

    public User toEntity() {
        User u = new User();
        u.setStatus(Status.ACTIVE);
        u.setEmail(getEmail());
        u.setUsername(getUsername());
        u.setPhone(getPhone());
        u.setPassword(getPassword());
        u.setRoles(getRoles());
        u.setFullName(getFullName());
        u.setProvider(getProvider());
        return u;
    }
}
