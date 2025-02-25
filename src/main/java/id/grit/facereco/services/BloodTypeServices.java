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
import id.grit.facereco.entity.BloodType;
import id.grit.facereco.model.request.BloodTypeRequest;
import id.grit.facereco.model.response.BloodTypeResponse;
import id.grit.facereco.repository.BloodTypeRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class BloodTypeServices {

    private BloodTypeRepository bloodTypeRepository;
    private JwtUtil jwtUtil;

    public BloodTypeServices(BloodTypeRepository bloodTypeRepository, JwtUtil jwtUtil) {
        this.bloodTypeRepository = bloodTypeRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(BloodTypeRequest bloodTypeRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (bloodTypeRequest.getBlood_type_name() != null &&
                    !bloodTypeRequest.getBlood_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data Blood Type";
                Date currentDate = new Date();
                bloodTypeRepository.save(mappingData(uuid_user_login, bloodTypeRequest, currentDate, currentDate));
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
            List<BloodType> list = bloodTypeRepository.findAll();
            List<BloodTypeResponse> listResponse = new ArrayList<>();

            for (BloodType bloodType : list) {
                BloodTypeResponse bloodTypeResponse = new BloodTypeResponse();
                bloodTypeResponse.setId(bloodType.getUuid());
                bloodTypeResponse.setBlood_type_name(bloodType.getBlood_type_name());
                bloodTypeResponse.setFlag_active_blood_type(bloodType.isFlag_active_blood_type());
                listResponse.add(bloodTypeResponse);
            }
            String message = "Berhasil Mengambil Data Blood Type";
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
            BloodTypeRequest bloodTypeRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() && bloodTypeRequest.getBlood_type_name() != null &&
                    !bloodTypeRequest.getBlood_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                Optional<BloodType> optional = bloodTypeRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Blood Type";
                    Date currentDate = new Date();
                    BloodType existingData = optional.get();
                    BloodType updateData = mappingData(uuid_user_login, bloodTypeRequest,
                            existingData.getDate_created(),
                            currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = bloodTypeRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Blood Type Tidak Ditemukan";
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

    private BloodType mappingData(String uuid_user, BloodTypeRequest bloodTypeRequest, Date createdDate,
            Date updatedDate) {
        BloodType entity = new BloodType();
        entity.setUuid(UUID.randomUUID());
        entity.setBlood_type_name(bloodTypeRequest.getBlood_type_name());
        entity.setFlag_active_blood_type(bloodTypeRequest.isFlag_active_blood_type());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }
}
