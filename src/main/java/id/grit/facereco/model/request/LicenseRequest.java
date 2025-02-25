package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LicenseRequest {
    private String license_name;
    private String license_serial_number;
    private String license_description;
}
