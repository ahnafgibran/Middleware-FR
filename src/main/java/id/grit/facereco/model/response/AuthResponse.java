package id.grit.facereco.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class AuthResponse {
    private String email;
    private String password;
    private String token;
}
