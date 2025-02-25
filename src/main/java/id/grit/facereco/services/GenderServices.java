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
import id.grit.facereco.entity.Gender;
import id.grit.facereco.model.request.GenderRequest;
import id.grit.facereco.model.response.GenderResponse;
import id.grit.facereco.repository.GenderRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class GenderServices {

    private GenderRepository genderRepository;
    private JwtUtil jwtUtil;

    public GenderServices(GenderRepository genderRespository, JwtUtil jwtUtil) {
        this.genderRepository = genderRespository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(GenderRequest genderRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (genderRequest.getGender_name() != null &&
                    !genderRequest.getGender_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data Gender";
                Date currentDate = new Date();
                genderRepository.save(mappingData(uuid_user_login, genderRequest, currentDate, currentDate));
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
            List<Gender> list = genderRepository.findAll();
            List<GenderResponse> listResponse = new ArrayList<>();

            for (Gender gender : list) {
                GenderResponse genderResponse = new GenderResponse();
                genderResponse.setId(gender.getUuid());
                genderResponse.setGender_name(gender.getGender_name());
                genderResponse.setFlag_active_gender(gender.isFlag_active_gender());
                listResponse.add(genderResponse);
            }
            String message = "Berhasil Mengambil Data Gender";
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

    public ResponseEntity<LinkedHashMap<String, Object>> serviceUpdateData(UUID uuid, GenderRequest genderRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() && genderRequest.getGender_name() != null &&
                    !genderRequest.getGender_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                Optional<Gender> optional = genderRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Gender";
                    Date currentDate = new Date();
                    Gender existingData = optional.get();
                    Gender updateData = mappingData(uuid_user_login, genderRequest, existingData.getDate_created(),
                            currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = genderRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Gender Tidak Ditemukan";
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

    private Gender mappingData(String uuid_user, GenderRequest genderRequest, Date createdDate, Date updatedDate) {
        Gender entity = new Gender();
        entity.setUuid(UUID.randomUUID());
        entity.setGender_name(genderRequest.getGender_name());
        entity.setFlag_active_gender(genderRequest.isFlag_active_gender());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }
}
