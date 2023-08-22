package tr.com.cemalaydin.dashboard.helpers.jedisdto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class JedisForgotPasswordDto {
    private String id;
    private Long date;
    private String dateStr;
    private Long renewKey;
}
