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
import id.grit.facereco.entity.MaritalType;
import id.grit.facereco.model.request.MaritalTypeRequest;
import id.grit.facereco.model.response.MaritalTypeResponse;
import id.grit.facereco.repository.MaritalTypeRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class MaritalTypeServices {

    private MaritalTypeRepository maritalTypeRepository;
    private JwtUtil jwtUtil;

    public MaritalTypeServices(MaritalTypeRepository maritalTypeRepository, JwtUtil jwtUtil) {
        this.maritalTypeRepository = maritalTypeRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(MaritalTypeRequest maritalTypeRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (maritalTypeRequest.getMarital_type_name() != null &&
                    !maritalTypeRequest.getMarital_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data Marital Type";
                Date currentDate = new Date();
                maritalTypeRepository.save(mappingData(uuid_user_login, maritalTypeRequest, currentDate, currentDate));
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
            List<MaritalType> list = maritalTypeRepository.findAll();
            List<MaritalTypeResponse> listResponse = new ArrayList<>();

            for (MaritalType maritalType : list) {
                MaritalTypeResponse maritalTypeResponse = new MaritalTypeResponse();
                maritalTypeResponse.setId(maritalType.getUuid());
                maritalTypeResponse.setMarital_type_name(maritalType.getMarital_type_name());
                maritalTypeResponse.setFlag_active_marital_type(maritalType.isFlag_active_marital_type());
                listResponse.add(maritalTypeResponse);
            }
            String message = "Berhasil Mengambil Data Marital Type";
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
            MaritalTypeRequest maritalTypeRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() && maritalTypeRequest.getMarital_type_name() != null &&
                    !maritalTypeRequest.getMarital_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                Optional<MaritalType> optional = maritalTypeRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Marital Type";
                    Date currentDate = new Date();
                    MaritalType existingData = optional.get();
                    MaritalType updateData = mappingData(uuid_user_login, maritalTypeRequest,
                            existingData.getDate_created(),
                            currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = maritalTypeRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Marital Type Tidak Ditemukan";
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

    private MaritalType mappingData(String uuid_user, MaritalTypeRequest maritalTypeRequest, Date createdDate,
            Date updatedDate) {
        MaritalType entity = new MaritalType();
        entity.setUuid(UUID.randomUUID());
        entity.setMarital_type_name(maritalTypeRequest.getMarital_type_name());
        entity.setFlag_active_marital_type(maritalTypeRequest.isFlag_active_marital_type());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }

}
