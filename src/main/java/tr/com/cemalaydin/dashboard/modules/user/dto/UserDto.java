package tr.com.cemalaydin.dashboard.modules.user.dto;

import tr.com.cemalaydin.dashboard.enums.AuthProvider;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private String pictureUrl;
    private boolean emailValidated;
    private boolean allowSendValidationEmailAgain;
    private AuthProvider provider;

}
