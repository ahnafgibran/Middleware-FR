package id.grit.facereco.model.request;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class OrganizationRequest {
    private String organization_photo;
    private String organization_name;
    private String organization_code;
    private String organization_office_address;
    private String organization_office_telephone;
    private String organization_email;
    private LocalDate organization_date_establishment;
    private UUID organization_jurisdiction_level_id;
    private Integer organization_total_personnel;
    private UUID organization_parent_id;
}
