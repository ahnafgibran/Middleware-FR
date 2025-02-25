package id.grit.facereco.model.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class StreamingDeviceRequest {
    private String device_name;
    private String ip_address;
    private String device_url_rtsp;
    private String latitude_device;
    private String longitude_device;
    private UUID device_organization_id;
    private UUID device_type_id;
    private UUID operation_id;
}
