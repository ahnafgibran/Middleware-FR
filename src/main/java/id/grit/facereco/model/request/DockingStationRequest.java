package id.grit.facereco.model.request;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class DockingStationRequest {
    private String docking_station_name;
    private String docking_station_ip_address;
    private UUID docking_station_organization_id;
}
