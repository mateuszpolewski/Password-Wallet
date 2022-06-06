package pl.application.passwordwallet.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordConfirmation {
    @Pattern(regexp = "^.{6,20}$", message = "Hasło powinno mieć od 6 do 20 znaków")
    private String firstPassword;
    @Pattern(regexp = "^.{6,20}$", message = "Hasło powinno mieć od 6 do 20 znaków")
    private String secondPassword;
    private String encodingMethod;
}
