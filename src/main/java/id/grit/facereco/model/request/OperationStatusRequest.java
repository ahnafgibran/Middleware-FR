package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class OperationStatusRequest {
    private String operation_status_name;
    private String operation_status_code;
    private boolean flag_active_operation;
}
