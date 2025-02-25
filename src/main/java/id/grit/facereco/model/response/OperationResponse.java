package id.grit.facereco.model.response;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class OperationResponse {
    private UUID id;
    private String operation_code;
    private String operation_name;
    private LocalDate operation_start_date;
    private LocalDate operation_end_date;
    private Integer operation_total_personnel;
    private String operation_patrol_area;
    private String operation_purpose;
    private String operation_supervisor_name;
    private String organization_name;
    private String operations_status;
    private UUID operation_organization_id;
    private UUID operation_status_id;
}
