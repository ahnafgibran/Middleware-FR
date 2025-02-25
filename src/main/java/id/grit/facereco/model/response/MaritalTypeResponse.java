package id.grit.facereco.model.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class MaritalTypeResponse {
    private UUID id;
    private String marital_type_name;
    private boolean flag_active_marital_type;
}
