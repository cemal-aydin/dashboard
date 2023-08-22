package tr.com.cemalaydin.dashboard.modules.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Valid
public class EmailValidateDto {

    @NotNull
    private String userId;
    @NotNull
    private String validateKey;

}
