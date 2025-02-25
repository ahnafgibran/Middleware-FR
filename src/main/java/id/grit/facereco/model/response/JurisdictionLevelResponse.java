package id.grit.facereco.model.response;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter

public class JurisdictionLevelResponse {
    private UUID id;
    private String jurisdiction_name;
    private String jurisdiction_code;
    private boolean flag_active_jurisdiction_level;
}
