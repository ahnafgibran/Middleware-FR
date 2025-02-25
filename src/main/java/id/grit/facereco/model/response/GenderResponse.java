package id.grit.facereco.model.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class GenderResponse {
    private UUID id;
    private String gender_name;
    private boolean flag_active_gender;
}
