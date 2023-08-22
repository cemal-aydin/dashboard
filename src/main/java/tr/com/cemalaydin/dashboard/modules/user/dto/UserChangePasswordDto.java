package tr.com.cemalaydin.dashboard.modules.user.dto;

import lombok.Data;

@Data
public class UserChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
