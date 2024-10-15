package evasio.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank(message = "Der Username darf nicht leer sein")
    private String username;

    @NotBlank(message = "Das Passwort darf nicht leer sein")
    String password;
}
