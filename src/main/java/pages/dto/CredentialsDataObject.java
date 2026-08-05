package pages.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsDataObject {
    private String userEmail;
    private String userPassword;
    private String confirmPassword;
}
