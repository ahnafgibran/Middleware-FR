package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ReligionTypeRequest {
    private String religion_type_name;
    private boolean flag_active_religion_type;
}
