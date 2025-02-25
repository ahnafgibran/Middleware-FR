package id.grit.facereco.model.response;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LicenseResponse {
    private UUID id;
    private String license_name;
    private String license_description;
    private String license_serial_number;
    private Date license_activated_date;
    private LocalDate license_expired_date;
}
