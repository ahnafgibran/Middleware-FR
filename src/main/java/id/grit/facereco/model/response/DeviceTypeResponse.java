package id.grit.facereco.model.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class DeviceTypeResponse {
    private UUID id;
    private String device_type_name;
    private boolean flag_active_device_type;
}
