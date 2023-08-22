package tr.com.cemalaydin.dashboard.modules.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Valid
public class ForgotPasswordRequestDto
{
    @NotNull
    private String username;
    @NotNull
    private Long renewKey;
    @NotNull
    private String newPassword;
}
