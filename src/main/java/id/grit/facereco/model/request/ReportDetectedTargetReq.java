package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportDetectedTargetReq {
    private String report_id;
    private String report_streaming_device_id;
    private String identity_template_id;
}
