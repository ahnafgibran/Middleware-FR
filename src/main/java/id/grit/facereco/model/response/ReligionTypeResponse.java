package id.grit.facereco.model.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ReligionTypeResponse {
    private UUID id;
    private String religion_type_name;
    private boolean flag_active_religion_type;
}
