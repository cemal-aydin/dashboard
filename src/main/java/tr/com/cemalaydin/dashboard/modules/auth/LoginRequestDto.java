package tr.com.cemalaydin.dashboard.modules.auth;

import tr.com.cemalaydin.dashboard.base.BaseClass;
import lombok.Data;

@Data
public class LoginRequestDto extends BaseClass {
    private String username;
    private String password;
    private String refreshJwt;
}
