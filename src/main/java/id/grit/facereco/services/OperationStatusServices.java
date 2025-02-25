package id.grit.facereco.services;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.grit.facereco.config.JwtUtil;
import id.grit.facereco.entity.OperationStatus;
import id.grit.facereco.model.request.OperationStatusRequest;
import id.grit.facereco.model.response.OperationStatusResponse;
import id.grit.facereco.repository.OperationStatusRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

@Service
public class OperationStatusServices {

    private OperationStatusRepository operationStatusRepository;
    private JwtUtil jwtUtil;

    public OperationStatusServices(OperationStatusRepository operationStatusRepository, JwtUtil jwtUtil) {
        this.operationStatusRepository = operationStatusRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(
            OperationStatusRequest operationStatusRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (operationStatusRequest.getOperation_status_name() != null &&
                    !operationStatusRequest.getOperation_status_name().isEmpty() &&
                    operationStatusRequest.getOperation_status_code() != null &&
                    !operationStatusRequest.getOperation_status_code().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {

                String message = "Berhasil Menambahkan Data Operation Status";
                Date currentDate = new Date();
                operationStatusRepository
                        .save(mappingData(uuid_user_login, operationStatusRequest, currentDate, currentDate));
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
            List<OperationStatus> list = operationStatusRepository.findAll();
            List<OperationStatusResponse> listResponse = new ArrayList<>();

            for (OperationStatus operationStatus : list) {
                OperationStatusResponse operationStatusResponse = new OperationStatusResponse();
                operationStatusResponse.setId(operationStatus.getUuid());
                operationStatusResponse.setOperation_status_name(operationStatus.getOperation_status_name());
                operationStatusResponse.setOperation_status_code(operationStatus.getOperation_status_code());
                operationStatusResponse
                        .setFlag_active_operation_status(operationStatus.isFlag_active_operation_status());
                listResponse.add(operationStatusResponse);
            }
            String message = "Berhasil Mengambil Data Operation Status";
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
            OperationStatusRequest operationStatusRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() &&
                    operationStatusRequest.getOperation_status_code() != null &&
                    !operationStatusRequest.getOperation_status_code().isEmpty() &&
                    operationStatusRequest.getOperation_status_name() != null &&
                    !operationStatusRequest.getOperation_status_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                Optional<OperationStatus> optional = operationStatusRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Operation Status";
                    Date currentDate = new Date();
                    OperationStatus existingData = optional.get();
                    OperationStatus updateData = mappingData(uuid_user_login, operationStatusRequest,
                            existingData.getDate_created(),
                            currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = operationStatusRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Operation Status Tidak Ditemukan";
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

    private OperationStatus mappingData(String uuid_user, OperationStatusRequest operationStatusRequest,
            Date createdDate,
            Date updatedDate) {
        OperationStatus entity = new OperationStatus();
        entity.setUuid(UUID.randomUUID());
        entity.setOperation_status_name(operationStatusRequest.getOperation_status_name());
        entity.setOperation_status_code(operationStatusRequest.getOperation_status_code());
        entity.setFlag_active_operation_status(operationStatusRequest.isFlag_active_operation());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }
}
