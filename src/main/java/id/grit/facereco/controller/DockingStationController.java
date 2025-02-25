package id.grit.facereco.controller;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import id.grit.facereco.model.request.DockingStationRequest;
import id.grit.facereco.services.DockingStationServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Validated
@CrossOrigin(maxAge = 3600)
@RequestMapping("/api/v2/docking-station")
@Tag(name = "Docking Station (Playback Video)", description = "")
public class DockingStationController {
    private final DockingStationServices dockingStationServices;

    public DockingStationController(DockingStationServices dockingStationServices) {
        this.dockingStationServices = dockingStationServices;
    }

    @PostMapping("/create")
    public ResponseEntity<LinkedHashMap<String, Object>> createDockingStation(
            @RequestBody(required = true) DockingStationRequest request, HttpServletRequest httpServletRequest) {
        return dockingStationServices.serviceCreateData(request, httpServletRequest);
    }

    @GetMapping("/get-list")
    public ResponseEntity<LinkedHashMap<String, Object>> getAllDockingStations(
            @RequestParam(required = false) String keywords,
            @RequestParam(required = false) String organizationId,
            @RequestParam(required = true, defaultValue = "0") Integer page,
            @RequestParam(required = true, defaultValue = "20") Integer size) {
        return dockingStationServices.serviceGetListData(keywords, organizationId, page, size);
    }

    @PutMapping("/update/{uuid}")
    public ResponseEntity<LinkedHashMap<String, Object>> updateDockingStation(@PathVariable UUID uuid,
            @RequestBody DockingStationRequest request, HttpServletRequest httpServletRequest) {
        return dockingStationServices.serviceUpdateData(uuid, request, httpServletRequest);
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<LinkedHashMap<String, Object>> deleteDockingStation(@PathVariable UUID uuid,
            HttpServletRequest httpServletRequest) {
        return dockingStationServices.serviceDeleteData(uuid, httpServletRequest);
    }

    @GetMapping("/access-to-url/{uuid}")
    public ResponseEntity<?> accessToDockingServer(@PathVariable("uuid") UUID uuid,
            HttpServletRequest httpServletRequest) {
        return dockingStationServices.serviceAccessToDockingServer(uuid, httpServletRequest);
    }

    @GetMapping(value = "/access-to-path/{uuid_dockstation}/get-data")
    public ResponseEntity<?> accessToPath(
            @PathVariable(name = "uuid_dockstation") UUID id_dockstation,
            @RequestParam(name = "key1", required = true) String key1,
            @RequestParam(name = "key2", required = true) String key2,
            @RequestParam(name = "key3", required = true) String key3,
            @RequestParam(name = "key4", required = true) String key4) {

        return dockingStationServices.serviceAccessToPath(id_dockstation, key1, key2, key3, key4);
    }

}
