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
import id.grit.facereco.entity.ReligionType;
import id.grit.facereco.model.request.ReligionTypeRequest;
import id.grit.facereco.model.response.ReligionTypeResponse;
import id.grit.facereco.repository.ReligionTypeRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ReligionTypeServices {

    private ReligionTypeRepository religionTypeRepository;
    private JwtUtil jwtUtil;

    public ReligionTypeServices(ReligionTypeRepository religionTypeRepository, JwtUtil jwtUtil) {
        this.religionTypeRepository = religionTypeRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(ReligionTypeRequest religionTypeRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (religionTypeRequest.getReligion_type_name() != null &&
                    !religionTypeRequest.getReligion_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data Religion Type";
                Date currentDate = new Date();
                religionTypeRepository
                        .save(mappingData(uuid_user_login, religionTypeRequest, currentDate, currentDate));
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
            List<ReligionType> list = religionTypeRepository.findAll();
            List<ReligionTypeResponse> listResponse = new ArrayList<>();

            for (ReligionType religionType : list) {
                ReligionTypeResponse religionTypeResponse = new ReligionTypeResponse();
                religionTypeResponse.setId(religionType.getUuid());
                religionTypeResponse.setReligion_type_name(religionType.getReligion_type_name());
                religionTypeResponse.setFlag_active_religion_type(religionType.isFlag_active_religion_type());
                listResponse.add(religionTypeResponse);
            }
            String message = "Berhasil Mengambil Data Religion Type";
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
            ReligionTypeRequest religionTypeRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() && religionTypeRequest.getReligion_type_name() != null &&
                    !religionTypeRequest.getReligion_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                Optional<ReligionType> optional = religionTypeRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Religion Type";
                    Date currentDate = new Date();
                    ReligionType existingData = optional.get();
                    ReligionType updateData = mappingData(uuid_user_login, religionTypeRequest,
                            existingData.getDate_created(),
                            currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = religionTypeRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Religion Type Tidak Ditemukan";
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

    private ReligionType mappingData(String uuid_user, ReligionTypeRequest religionTypeRequest, Date createdDate,
            Date updatedDate) {
        ReligionType entity = new ReligionType();
        entity.setUuid(UUID.randomUUID());
        entity.setReligion_type_name(religionTypeRequest.getReligion_type_name());
        entity.setFlag_active_religion_type(religionTypeRequest.isFlag_active_religion_type());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }
}
