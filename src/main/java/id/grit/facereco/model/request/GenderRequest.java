package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class GenderRequest {
    private String gender_name;
    private boolean flag_active_gender;
}
