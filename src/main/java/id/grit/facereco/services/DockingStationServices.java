// DockingStationService
package id.grit.facereco.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.grit.facereco.config.JwtUtil;
import id.grit.facereco.entity.DockingEntity;
import id.grit.facereco.entity.Organization;
import id.grit.facereco.model.request.DockingStationRequest;
import id.grit.facereco.model.response.DockingStationResponse;
import id.grit.facereco.repository.DockingStationRepository;
import id.grit.facereco.repository.OrganizationRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class DockingStationServices {

    final DockingStationRepository dockingStationRepository;
    final JwtUtil jwtUtil;
    final OrganizationRepository organizationRepository;

    public DockingStationServices(DockingStationRepository dockingStationRepository, JwtUtil jwtUtil,
            OrganizationRepository organizationRepository) {
        this.dockingStationRepository = dockingStationRepository;
        this.jwtUtil = jwtUtil;
        this.organizationRepository = organizationRepository;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(DockingStationRequest dockingStationRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (dockingStationRequest.getDocking_station_name() != null &&
                    !dockingStationRequest.getDocking_station_name().isEmpty() &&
                    dockingStationRequest.getDocking_station_ip_address() != null &&
                    !dockingStationRequest.getDocking_station_ip_address().isEmpty() &&
                    dockingStationRequest.getDocking_station_organization_id() != null &&
                    !dockingStationRequest.getDocking_station_organization_id().toString().isEmpty()) {
                Optional<Organization> organization = organizationRepository
                        .findByUuid(dockingStationRequest.getDocking_station_organization_id());
                if (organization.isEmpty()) {
                    String message = "Organization tidak ditemukan";
                    response.put("status", "ERROR");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                } else {
                    String message = "Berhasil Menambahkan Data Docking Station";
                    Date currentDate = new Date();
                    dockingStationRepository
                            .save(mappingData(uuid_user_login, dockingStationRequest, currentDate, currentDate));
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                String message = "Request Tidak Sesuai";
                response.put("status", "ERROR");
                response.put("message", message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceGetListData(String keywordPencarian,
            String organizationId,
            Integer page,
            Integer size) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<DockingEntity> listDockingStation = null;
            if (keywordPencarian != null && !keywordPencarian.toString().isEmpty()) {
                listDockingStation = organizationId != null && !organizationId.toString().isEmpty()
                        ? dockingStationRepository.findDataByKeywordAndOrgId(keywordPencarian,
                                UUID.fromString(organizationId), pageable)
                        : dockingStationRepository.findDataByKeyword(keywordPencarian, pageable);
                List<DockingStationResponse> listResponse = new ArrayList<>();
                for (DockingEntity dockingEntity : listDockingStation) {

                    Optional<Organization> organization = organizationRepository
                            .findByUuid(dockingEntity.getDocking_station_organization_id());

                    DockingStationResponse dockingStationResponse = new DockingStationResponse();
                    dockingStationResponse.setId(dockingEntity.getUuid());
                    dockingStationResponse.setDocking_station_name(dockingEntity.getDocking_station_name());
                    dockingStationResponse.setDocking_station_ip_address(dockingEntity.getDocking_station_ip_address());
                    dockingStationResponse
                            .setDocking_station_organization_id(dockingEntity.getDocking_station_organization_id());
                    dockingStationResponse.setDocking_station_organization_name(
                            organization.isPresent() ? organization.get().getOrganization_name() : "-");
                    listResponse.add(dockingStationResponse);
                }
                String message = "Berhasil Mengambil Data Docking Station";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listDockingStation));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                listDockingStation = organizationId != null &&
                        !organizationId.toString().isEmpty()
                                ? dockingStationRepository.findAllDataByOrgId(UUID.fromString(organizationId),
                                        pageable)
                                : dockingStationRepository.findAllData(pageable);
                List<DockingStationResponse> listResponse = new ArrayList<>();
                for (DockingEntity dockingEntity : listDockingStation) {

                    Optional<Organization> organization = organizationRepository
                            .findByUuid(dockingEntity.getDocking_station_organization_id());
                    DockingStationResponse dockingStationResponse = new DockingStationResponse();
                    dockingStationResponse.setId(dockingEntity.getUuid());
                    dockingStationResponse.setDocking_station_name(dockingEntity.getDocking_station_name());
                    dockingStationResponse.setDocking_station_ip_address(dockingEntity.getDocking_station_ip_address());
                    dockingStationResponse
                            .setDocking_station_organization_id(dockingEntity.getDocking_station_organization_id());
                    dockingStationResponse.setDocking_station_organization_name(
                            organization.isPresent() ? organization.get().getOrganization_name() : "-");
                    listResponse.add(dockingStationResponse);
                }
                String message = "Berhasil Mengambil Data Docking Station";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listDockingStation));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceUpdateData(UUID uuid,
            DockingStationRequest dockingStationRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);

        try {
            if (uuid != null && !uuid.toString().isEmpty() && dockingStationRequest.getDocking_station_name() != null
                    && !dockingStationRequest.getDocking_station_name().isEmpty()
                    && dockingStationRequest.getDocking_station_ip_address() != null
                    && !dockingStationRequest.getDocking_station_ip_address().isEmpty()
                    && dockingStationRequest.getDocking_station_organization_id() != null
                    && !dockingStationRequest.getDocking_station_organization_id().toString().isEmpty()) {
                Optional<DockingEntity> optional = dockingStationRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    Optional<Organization> organization = organizationRepository
                            .findByUuid(dockingStationRequest.getDocking_station_organization_id());
                    if (organization.isEmpty()) {
                        String message = "Organization tidak ditemukan";
                        response.put("status", "ERROR");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    } else {
                        String message = "Berhasil Mengubah Data Docking Station";
                        Date currentDate = new Date();
                        DockingEntity existingData = optional.get();
                        DockingEntity updateData = mappingData(uuid_user_login, dockingStationRequest, currentDate,
                                currentDate);

                        // Set Value
                        updateData.setId(existingData.getId());
                        updateData.setUuid(uuid);

                        // save the update entity
                        updateData = dockingStationRepository.save(updateData);
                        response.put("status", "SUKSES");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    }

                } else {
                    String message = "Docking Station Tidak Ditemukan";
                    response.put("status", "ERROR");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                }
            } else {
                String message = "Request Tidak Sesuai";
                response.put("status", "ERROR");
                response.put("message", message);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceDeleteData(UUID uuid,
            HttpServletRequest httpServletRequest) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            Optional<DockingEntity> optional = dockingStationRepository.findByUuid(uuid);
            if (optional.isPresent()) {
                dockingStationRepository.delete(optional.get());
                String message = "Berhasil Menghapus Data Docking Station";
                response.put("status", "SUKSES");
                response.put("message", message);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                String message = "Data Docking Station Tidak Ditemukan";
                response.put("status", "ERROR");
                response.put("message", message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceAccessToDockingServer(UUID uuid,
            HttpServletRequest httpServletRequest) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        ObjectMapper objectMapper = new ObjectMapper(); // Untuk memproses JSON

        try {
            Optional<DockingEntity> dockStation = dockingStationRepository.findByUuid(uuid);

            if (dockStation.isPresent()) {
                String accessUrl = dockStation.get().getDocking_station_ip_address();
                System.out.println("Check URL: " + accessUrl);

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Object> entity = new HttpEntity<>(headers);
                ResponseEntity<String> result = restTemplate.exchange(accessUrl, HttpMethod.GET, entity, String.class);

                System.out.println("Check Result: " + result.getBody());

                List<Map<String, Object>> jsonData = objectMapper.readValue(result.getBody(), new TypeReference<>() {
                });

                response.put("status", "SUKSES");
                response.put("message", "Berhasil Mengakses Server Docking Station");
                response.put("data", jsonData);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                String message = "Data Docking Station Tidak Ditemukan dengan UUID = " + uuid;
                response.put("status", "ERROR");
                response.put("message", message);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (JsonProcessingException | RestClientException e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    public ResponseEntity<Map<String, Object>> serviceAccessToPath(
            UUID id_dockstation,
            String key1,
            String key2,
            String key3,
            String key4) {

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        try {
            Optional<DockingEntity> dockStation = dockingStationRepository.findByUuid(id_dockstation);
            if (dockStation.isPresent()) {
                String accessUrl = dockStation.get().getDocking_station_ip_address();

                if (!key1.equalsIgnoreCase("null"))
                    accessUrl += "/" + key1;
                if (!key2.equalsIgnoreCase("null"))
                    accessUrl += "/" + key2;
                if (!key3.equalsIgnoreCase("null"))
                    accessUrl += "/" + key3;
                if (!key4.equalsIgnoreCase("null"))
                    accessUrl += "/" + key4;

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Object> entity = new HttpEntity<>(headers);
                ResponseEntity<String> result = restTemplate.exchange(accessUrl, HttpMethod.GET, entity, String.class);

                JSONArray jsonArray = new JSONArray(result.getBody());
                JSONObject dataAll = new JSONObject();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject dtArr = jsonArray.getJSONObject(i);

                    String linkUrl = accessUrl + "/" + dtArr.get("name");
                    dtArr.put("url", linkUrl);

                    dataAll.append("datas", dtArr);
                }

                dataAll.put("status", HttpStatus.OK.value());
                return ResponseEntity.ok(dataAll.toMap());
            } else {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "Docking Station with UUID = " + id_dockstation + " Not Found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private DockingEntity mappingData(String uuid_user, DockingStationRequest dockingStationRequest, Date createdDate,
            Date updatedDate) {
        DockingEntity entity = new DockingEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setDocking_station_name(dockingStationRequest.getDocking_station_name());
        entity.setDocking_station_ip_address(dockingStationRequest.getDocking_station_ip_address());
        entity.setDocking_station_organization_id(dockingStationRequest.getDocking_station_organization_id());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }

    private LinkedHashMap<String, Object> getPageInfo(Page<DockingEntity> listDockingStation) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("size", listDockingStation.getSize());
        response.put("totalItems", listDockingStation.getTotalElements());
        response.put("totalPages", listDockingStation.getTotalPages());
        response.put("currentPage", listDockingStation.getNumber());
        return response;
    }

}
