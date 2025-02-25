package id.grit.facereco.model.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class StreamingDeviceResponse {
    private UUID id;
    private String device_name;
    private String ip_address;
    private String device_url_rtsp;
    private String latitude_device;
    private String longitude_device;
    private String organization_name;
    private String tipe_perangkat;
    private String operation_name;
    private UUID device_organization_id;
    private UUID device_type_id;
    private UUID operation_id;
}
