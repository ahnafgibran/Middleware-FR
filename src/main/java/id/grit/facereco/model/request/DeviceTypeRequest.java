package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class DeviceTypeRequest {
    private String device_type_name;
    private boolean flag_active_device_type;
}
