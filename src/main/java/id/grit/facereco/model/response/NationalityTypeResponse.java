package id.grit.facereco.model.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class NationalityTypeResponse {
    private UUID id;
    private String nationality_type_name;
    private boolean flag_active_nationality_type;
}
