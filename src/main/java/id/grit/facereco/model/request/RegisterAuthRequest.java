package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class RegisterAuthRequest {
    private String email;
    private String fullName;
    private String phone;
    private String password;
    private String confirmPassword;
    private String user_gender_id;
    private String user_organization_id;
    private String user_permission_id;
}
