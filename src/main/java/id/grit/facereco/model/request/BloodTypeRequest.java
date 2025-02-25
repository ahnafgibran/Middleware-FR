package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class BloodTypeRequest {
    private String blood_type_name;
    private boolean flag_active_blood_type;
}
