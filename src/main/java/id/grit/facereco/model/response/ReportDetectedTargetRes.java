package id.grit.facereco.model.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportDetectedTargetRes {
    private UUID id;
    private ReportResponse report_id;
    private StreamingDeviceResponse report_streaming_device_id;
    private PersonEngineRes identity_template_id;
}
