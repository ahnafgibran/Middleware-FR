package id.grit.facereco.model.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class OperationStatusResponse {
    private UUID id;
    private String operation_status_name;
    private String operation_status_code;
    private boolean flag_active_operation_status;
}
