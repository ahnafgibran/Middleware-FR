package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class JurisdictionLevelRequest {
    private String jurisdiction_name;
    private String jurisdiction_code;
    private boolean flag_active_jurisdiction;
}
