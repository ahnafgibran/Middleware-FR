package id.grit.facereco.model.response;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DockingStationResponse {
    private UUID id;
    private String docking_station_name;
    private String docking_station_ip_address;
    private String docking_station_organization_name;
    private UUID docking_station_organization_id;
}
