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
import id.grit.facereco.entity.NationalityType;
import id.grit.facereco.model.request.NationalityTypeRequest;
import id.grit.facereco.model.response.NationalityTypeResponse;
import id.grit.facereco.repository.NationalityTypeRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class NationalityTypeServices {

    private NationalityTypeRepository nationalityTypeRepository;
    private JwtUtil jwtUtil;

    public NationalityTypeServices(NationalityTypeRepository nationalityTypeRepository, JwtUtil jwtUtil) {
        this.nationalityTypeRepository = nationalityTypeRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(
            NationalityTypeRequest nationalityTypeRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (nationalityTypeRequest.getNationality_type_name() != null &&
                    !nationalityTypeRequest.getNationality_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data Nationality Type";
                Date currentDate = new Date();
                nationalityTypeRepository
                        .save(mappingData(uuid_user_login, nationalityTypeRequest, currentDate, currentDate));
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
            List<NationalityType> list = nationalityTypeRepository.findAll();
            List<NationalityTypeResponse> listResponse = new ArrayList<>();

            for (NationalityType nationalityType : list) {
                NationalityTypeResponse nationalityTypeResponse = new NationalityTypeResponse();
                nationalityTypeResponse.setId(nationalityType.getUuid());
                nationalityTypeResponse.setNationality_type_name(nationalityType.getNationality_type_name());
                nationalityTypeResponse
                        .setFlag_active_nationality_type(nationalityType.isFlag_active_nationality_type());
                listResponse.add(nationalityTypeResponse);
            }
            String message = "Berhasil Mengambil Data Nationality Type";
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
            NationalityTypeRequest nationalityTypeRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() && nationalityTypeRequest.getNationality_type_name() != null &&
                    !nationalityTypeRequest.getNationality_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                Optional<NationalityType> optional = nationalityTypeRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Nationality Type";
                    Date currentDate = new Date();
                    NationalityType existingData = optional.get();
                    NationalityType updateData = mappingData(uuid_user_login, nationalityTypeRequest,
                            existingData.getDate_created(),
                            currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = nationalityTypeRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Nationality Type Tidak Ditemukan";
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

    private NationalityType mappingData(String uuid_user, NationalityTypeRequest nationalityTypeRequest,
            Date createdDate,
            Date updatedDate) {
        NationalityType entity = new NationalityType();
        entity.setUuid(UUID.randomUUID());
        entity.setNationality_type_name(nationalityTypeRequest.getNationality_type_name());
        entity.setFlag_active_nationality_type(nationalityTypeRequest.isFlag_active_nationality_type());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }
}
