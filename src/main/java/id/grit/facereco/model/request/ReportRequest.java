package id.grit.facereco.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequest {
    private String video_file_name;
    private String name_camera;
    private boolean is_ip_camera;
    private boolean is_bwc;
    private String report_organization_id;
}
