package id.grit.facereco.services;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import id.grit.facereco.config.JwtUtil;
import id.grit.facereco.entity.JurisdictionLevel;
import id.grit.facereco.model.request.JurisdictionLevelRequest;
import id.grit.facereco.model.response.JurisdictionLevelResponse;
import id.grit.facereco.repository.JurisdictionRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JurisdictionLevelServices {

    private JurisdictionRepository jurisdictionRepository;
    private JwtUtil jwtUtil;

    public JurisdictionLevelServices(JurisdictionRepository jurisdictionRepository, JwtUtil jwtUtil) {
        this.jurisdictionRepository = jurisdictionRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(
            JurisdictionLevelRequest jurisdictionLevelRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (jurisdictionLevelRequest.getJurisdiction_name() != null &&
                    !jurisdictionLevelRequest.getJurisdiction_name().isEmpty() &&
                    jurisdictionLevelRequest.getJurisdiction_code() != null &&
                    !jurisdictionLevelRequest.getJurisdiction_code().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data Jurisdiction Level";
                Date currentDate = new Date();
                jurisdictionRepository
                        .save(mappingData(uuid_user_login, jurisdictionLevelRequest, currentDate, currentDate));
                response.put("status", "SUKSES");
                response.put("message", message);
                return ResponseEntity.status(HttpStatus.OK).body(response);
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

    public ResponseEntity<LinkedHashMap<String, Object>> serviceGetListData() {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            List<JurisdictionLevel> list = jurisdictionRepository.findAll();
            List<JurisdictionLevelResponse> listResponse = new ArrayList<>();

            for (JurisdictionLevel jurisdictionLevel : list) {
                JurisdictionLevelResponse jurisdictionLevelResponse = new JurisdictionLevelResponse();
                jurisdictionLevelResponse.setId(jurisdictionLevel.getUuid());
                jurisdictionLevelResponse.setJurisdiction_code(jurisdictionLevel.getJurisdiction_code());
                jurisdictionLevelResponse.setJurisdiction_name(jurisdictionLevel.getJurisdiction_name());
                jurisdictionLevelResponse
                        .setFlag_active_jurisdiction_level(jurisdictionLevel.isFlag_active_jurisdiction());
                listResponse.add(jurisdictionLevelResponse);
            }
            String message = "Berhasil Mengambil Data Jurisdiction Level";
            response.put("status", "SUKSES");
            response.put("message", message);
            response.put("data", listResponse);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceUpdateData(UUID uuid,
            JurisdictionLevelRequest jurisdictionLevelRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() && jurisdictionLevelRequest.getJurisdiction_name() != null &&
                    !jurisdictionLevelRequest.getJurisdiction_name().isEmpty() &&
                    jurisdictionLevelRequest.getJurisdiction_code() != null &&
                    !jurisdictionLevelRequest.getJurisdiction_code().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                Optional<JurisdictionLevel> optional = jurisdictionRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Jurisdiction Level";
                    Date currentDate = new Date();
                    JurisdictionLevel existingData = optional.get();
                    JurisdictionLevel updateData = mappingData(uuid_user_login, jurisdictionLevelRequest,
                            existingData.getDate_created(),
                            currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = jurisdictionRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Jurisdiction Level Tidak Ditemukan";
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

    private JurisdictionLevel mappingData(String uuid_user, JurisdictionLevelRequest jurisdictionLevelRequest,
            Date createdDate,
            Date updatedDate) {
        JurisdictionLevel entity = new JurisdictionLevel();
        entity.setUuid(UUID.randomUUID());
        entity.setJurisdiction_name(jurisdictionLevelRequest.getJurisdiction_name());
        entity.setJurisdiction_code(jurisdictionLevelRequest.getJurisdiction_code());
        entity.setFlag_active_jurisdiction(jurisdictionLevelRequest.isFlag_active_jurisdiction());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }

}
