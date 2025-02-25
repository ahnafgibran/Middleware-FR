package id.grit.facereco.model.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class BloodTypeResponse {
    private UUID id;
    private String blood_type_name;
    private boolean flag_active_blood_type;
}
