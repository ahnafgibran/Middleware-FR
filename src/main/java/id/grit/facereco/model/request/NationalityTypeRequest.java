package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class NationalityTypeRequest {
    private String nationality_type_name;
    private boolean flag_active_nationality_type;
}
