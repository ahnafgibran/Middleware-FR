package id.grit.facereco.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterAuthResponse {
    private String email;
    private String fullName;
    private String phone;
    private String user_gender_id;
    private String user_organization_id;
    private String user_permission_id;
    @JsonProperty("is_active")
    private boolean is_active;
}
