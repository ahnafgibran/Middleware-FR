package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class MaritalTypeRequest {
    private String marital_type_name;
    private boolean flag_active_marital_type;
}
