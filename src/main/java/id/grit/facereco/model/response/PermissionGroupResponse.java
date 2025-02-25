package id.grit.facereco.model.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class PermissionGroupResponse {
    private UUID id;
    private String permission_name;
    private String permission_flag_object;
}
