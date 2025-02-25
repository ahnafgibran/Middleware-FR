package id.grit.facereco.services;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.grit.facereco.config.JwtUtil;
import id.grit.facereco.entity.Operation;
import id.grit.facereco.entity.OperationStatus;
import id.grit.facereco.entity.Organization;
import id.grit.facereco.model.request.OperationRequest;
import id.grit.facereco.model.response.OperationResponse;
import id.grit.facereco.repository.OperationRepository;
import id.grit.facereco.repository.OperationStatusRepository;
import id.grit.facereco.repository.OrganizationRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

@Service
public class OperationServices {

    private OperationRepository operationRepository;
    private OrganizationRepository organizationRepository;
    private OperationStatusRepository operationStatusRepository;
    private JwtUtil jwtUtil;

    public OperationServices(OperationRepository operationRepository, OrganizationRepository organizationRepository,
            OperationStatusRepository operationStatusRepository,
            JwtUtil jwtUtil) {
        this.operationRepository = operationRepository;
        this.organizationRepository = organizationRepository;
        this.operationStatusRepository = operationStatusRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(OperationRequest operationRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (operationRequest.getOperation_code() != null &&
                    !operationRequest.getOperation_code().toString().isEmpty() &&
                    operationRequest.getOperation_name() != null &&
                    !operationRequest.getOperation_name().toString().isEmpty() &&
                    operationRequest.getOperation_start_date() != null &&
                    !operationRequest.getOperation_start_date().toString().isEmpty() &&
                    operationRequest.getOperation_end_date() != null &&
                    !operationRequest.getOperation_end_date().toString().isEmpty() &&
                    operationRequest.getOperation_total_personnel() != null &&
                    !operationRequest.getOperation_total_personnel().toString().isEmpty() &&
                    operationRequest.getOperation_patrol_area() != null &&
                    !operationRequest.getOperation_patrol_area().toString().isEmpty() &&
                    operationRequest.getOperation_purpose() != null &&
                    !operationRequest.getOperation_purpose().toString().isEmpty() &&
                    operationRequest.getOperation_supervisor_name() != null &&
                    !operationRequest.getOperation_supervisor_name().toString().isEmpty() &&
                    operationRequest.getOperation_organization_id() != null &&
                    !operationRequest.getOperation_organization_id().toString().isEmpty() &&
                    operationRequest.getOperation_status_id() != null &&
                    !operationRequest.getOperation_status_id().toString().isEmpty()) {

                Optional<Organization> organization = organizationRepository
                        .findByUuid(operationRequest.getOperation_organization_id());

                Optional<OperationStatus> operationStatus = operationStatusRepository
                        .findByUuid(operationRequest.getOperation_status_id());

                if (organization.isEmpty()) {
                    String message = "Organization tidak ditemukan";
                    response.put("status", "ERROR");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                } else if (operationStatus.isEmpty()) {
                    String message = "Status Operasi tidak ditemukan";
                    response.put("status", "ERROR");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                } else {
                    if (operationStatus.get().isFlag_active_operation_status()) {
                        String message = "Berhasil Menambahkan Data Operation";
                        Date currentDate = new Date();
                        operationRepository
                                .save(mappingData(uuid_user_login, operationRequest, currentDate, currentDate));
                        response.put("status", "SUKSES");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                    } else {
                        String message = "Status Operasi sudah tidak aktif";
                        response.put("status", "ERROR");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
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

    public ResponseEntity<LinkedHashMap<String, Object>> serviceGetDataOperation(String keywordPencarian,
            String organizationId,
            Integer page,
            Integer size) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Operation> listOperation = null;
            if (keywordPencarian != null && !keywordPencarian.toString().isEmpty()) {

                listOperation = organizationId != null && !organizationId.toString().isEmpty()
                        ? operationRepository.findDataByKeywordAndOrgId(keywordPencarian,
                                UUID.fromString(organizationId), pageable)
                        : operationRepository.findDataByKeyword(keywordPencarian, pageable);
                List<OperationResponse> listResponse = new ArrayList<>();
                for (Operation operation : listOperation) {
                    Optional<Organization> organization = organizationRepository
                            .findByUuid(operation.getOperation_organization_id());

                    Optional<OperationStatus> operationStatus = operationStatusRepository
                            .findByUuid(operation.getOperation_status_id());
                    OperationResponse operationResponse = new OperationResponse();
                    operationResponse.setId(operation.getUuid());
                    operationResponse.setOperation_code(operation.getOperation_code());
                    operationResponse.setOperation_name(operation.getOperation_name());
                    operationResponse.setOperation_start_date(operation.getOperation_start_date());
                    operationResponse.setOperation_end_date(operation.getOperation_end_date());
                    operationResponse.setOperation_total_personnel(operation.getOperation_total_personnel());
                    operationResponse.setOperation_patrol_area(operation.getOperation_patrol_area());
                    operationResponse.setOperation_purpose(operation.getOperation_purpose());
                    operationResponse.setOperation_supervisor_name(operation.getOperation_supervisor_name());
                    operationResponse.setOperation_organization_id(operation.getOperation_organization_id());
                    operationResponse.setOperation_status_id(operation.getOperation_status_id());
                    operationResponse.setOperations_status(
                            operationStatus.isPresent() ? operationStatus.get().getOperation_status_name() : "-");
                    operationResponse.setOrganization_name(
                            organization.isPresent() ? organization.get().getOrganization_name() : "-");
                    listResponse.add(operationResponse);
                }
                String message = "Berhasil Mengambil Data Operation";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listOperation));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                listOperation = organizationId != null && !organizationId.toString().isEmpty()
                        ? operationRepository.findAllDataByOrgId(UUID.fromString(organizationId), pageable)
                        : operationRepository.findAllData(pageable);
                List<OperationResponse> listResponse = new ArrayList<>();
                for (Operation operation : listOperation) {
                    Optional<Organization> organization = organizationRepository
                            .findByUuid(operation.getOperation_organization_id());

                    Optional<OperationStatus> operationStatus = operationStatusRepository
                            .findByUuid(operation.getOperation_status_id());
                    OperationResponse operationResponse = new OperationResponse();
                    operationResponse.setId(operation.getUuid());
                    operationResponse.setOperation_code(operation.getOperation_code());
                    operationResponse.setOperation_name(operation.getOperation_name());
                    operationResponse.setOperation_start_date(operation.getOperation_start_date());
                    operationResponse.setOperation_end_date(operation.getOperation_end_date());
                    operationResponse.setOperation_total_personnel(operation.getOperation_total_personnel());
                    operationResponse.setOperation_patrol_area(operation.getOperation_patrol_area());
                    operationResponse.setOperation_purpose(operation.getOperation_purpose());
                    operationResponse.setOperation_supervisor_name(operation.getOperation_supervisor_name());
                    operationResponse.setOperation_organization_id(operation.getOperation_organization_id());
                    operationResponse.setOperation_status_id(operation.getOperation_status_id());
                    operationResponse.setOperations_status(
                            operationStatus.isPresent() ? operationStatus.get().getOperation_status_name() : "-");
                    operationResponse.setOrganization_name(
                            organization.isPresent() ? organization.get().getOrganization_name() : "-");
                    listResponse.add(operationResponse);
                }
                String message = "Berhasil Mengambil Data Operation";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listOperation));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceDeleteData(UUID uuid) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty()) {
                Optional<Operation> optional = operationRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Data Operation Berhasil Dihapus";
                    operationRepository.delete(optional.get());
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Operation Tidak Ditemukan";
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
        } catch (DataIntegrityViolationException e) {
            Optional<Operation> optional = operationRepository.findByUuid(uuid);
            String operation_name = optional.map(Operation::getOperation_name).orElse("Unknown");

            String message = "Gagal menghapus " + operation_name
                    + " , karena data ini masih digunakan untuk referensi data lain ";
            response.put("status", "ERROR");
            response.put("message", message);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceUpdateData(UUID uuid,
            OperationRequest operationRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() &&
                    operationRequest.getOperation_code() != null &&
                    !operationRequest.getOperation_code().toString().isEmpty() &&
                    operationRequest.getOperation_name() != null &&
                    !operationRequest.getOperation_name().toString().isEmpty() &&
                    operationRequest.getOperation_start_date() != null &&
                    !operationRequest.getOperation_start_date().toString().isEmpty() &&
                    operationRequest.getOperation_end_date() != null &&
                    !operationRequest.getOperation_end_date().toString().isEmpty() &&
                    operationRequest.getOperation_total_personnel() != null &&
                    !operationRequest.getOperation_total_personnel().toString().isEmpty() &&
                    operationRequest.getOperation_patrol_area() != null &&
                    !operationRequest.getOperation_patrol_area().toString().isEmpty() &&
                    operationRequest.getOperation_purpose() != null &&
                    !operationRequest.getOperation_purpose().toString().isEmpty() &&
                    operationRequest.getOperation_supervisor_name() != null &&
                    !operationRequest.getOperation_supervisor_name().toString().isEmpty() &&
                    operationRequest.getOperation_organization_id() != null &&
                    !operationRequest.getOperation_organization_id().toString().isEmpty() &&
                    operationRequest.getOperation_status_id() != null &&
                    !operationRequest.getOperation_status_id().toString().isEmpty()) {
                Optional<Operation> optional = operationRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    Optional<Organization> organization = organizationRepository
                            .findByUuid(operationRequest.getOperation_organization_id());
                    Optional<OperationStatus> operationStatus = operationStatusRepository
                            .findByUuid(operationRequest.getOperation_status_id());
                    if (organization.isEmpty()) {
                        String message = "Organization tidak ditemukan";
                        response.put("status", "ERROR");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    } else if (operationStatus.isEmpty()) {
                        String message = "Status Operasi tidak ditemukan";
                        response.put("status", "ERROR");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    } else {
                        if (operationStatus.get().isFlag_active_operation_status()) {
                            String message = "Berhasil Menambahkan Data Operation";
                            Date currentDate = new Date();
                            operationRepository
                                    .save(mappingData(uuid_user_login, operationRequest, currentDate, currentDate));
                            response.put("status", "SUKSES");
                            response.put("message", message);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        } else {
                            String message = "Berhasil Mengubah Data Operation";
                            Date currentDate = new Date();
                            Operation existingData = optional.get();
                            Operation updateData = mappingData(uuid_user_login, operationRequest,
                                    existingData.getDate_created(), currentDate);

                            // Set Value
                            updateData.setId(existingData.getId());
                            updateData.setUuid(uuid);

                            // save the update entity
                            updateData = operationRepository.save(updateData);
                            response.put("status", "SUKSES");
                            response.put("message", message);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        }
                    }
                } else {
                    String message = "Data Operation Tidak Ditemukan";
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

    private Operation mappingData(String uuid_user, OperationRequest operationRequest, Date createdDate,
            Date updatedDate) {
        Operation entity = new Operation();
        entity.setUuid(UUID.randomUUID());
        entity.setOperation_code(operationRequest.getOperation_code());
        entity.setOperation_name(operationRequest.getOperation_name());
        entity.setOperation_start_date(operationRequest.getOperation_start_date());
        entity.setOperation_end_date(operationRequest.getOperation_end_date());
        entity.setOperation_total_personnel(operationRequest.getOperation_total_personnel());
        entity.setOperation_patrol_area(operationRequest.getOperation_patrol_area());
        entity.setOperation_purpose(operationRequest.getOperation_purpose());
        entity.setOperation_supervisor_name(operationRequest.getOperation_supervisor_name());
        entity.setOperation_organization_id(operationRequest.getOperation_organization_id());
        entity.setOperation_status_id(operationRequest.getOperation_status_id());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }

    private LinkedHashMap<String, Object> getPageInfo(Page<Operation> listOperation) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("size", listOperation.getSize());
        response.put("totalItems", listOperation.getTotalElements());
        response.put("totalPages", listOperation.getTotalPages());
        response.put("currentPage", listOperation.getNumber());
        return response;
    }
}
