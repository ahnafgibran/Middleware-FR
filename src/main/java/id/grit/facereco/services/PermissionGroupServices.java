package id.grit.facereco.services;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import id.grit.facereco.config.JwtUtil;
import id.grit.facereco.entity.PermissionGroup;
import id.grit.facereco.model.request.PermisionGroupRequest;
import id.grit.facereco.model.response.PermissionGroupResponse;
import id.grit.facereco.repository.PermissionGroupRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class PermissionGroupServices {

    private PermissionGroupRepository permissionGroupRepository;
    private JwtUtil jwtUtil;

    public PermissionGroupServices(PermissionGroupRepository permissionGroupRepository, JwtUtil jwtUtil) {
        this.permissionGroupRepository = permissionGroupRepository;

        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(PermisionGroupRequest permisionGroupRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (permisionGroupRequest.getPermission_name() != null &&
                    !permisionGroupRequest.getPermission_name().toString().isEmpty() &&
                    permisionGroupRequest.getPermission_flag_object() != null &&
                    !permisionGroupRequest.getPermission_flag_object().toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data Permission Group";
                Date currentDate = new Date();
                permissionGroupRepository
                        .save(mappingData(uuid_user_login, permisionGroupRequest, currentDate, currentDate));
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

    public ResponseEntity<LinkedHashMap<String, Object>> serviceGetDataPermissionGroup(String keywordPencarian,
            String organizationId,
            Integer page,
            Integer size) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<PermissionGroup> listPermissionGroup = null;
            if (keywordPencarian != null && !keywordPencarian.toString().isEmpty()) {
                listPermissionGroup = organizationId != null && !organizationId.toString().isEmpty()
                        ? permissionGroupRepository.findDataByKeywordAndOrgId(keywordPencarian,
                                UUID.fromString(organizationId), pageable)
                        : permissionGroupRepository.findDataByKeyword(keywordPencarian, pageable);
                List<PermissionGroupResponse> listResponse = new ArrayList<>();
                for (PermissionGroup permissionGroup : listPermissionGroup) {

                    PermissionGroupResponse permissionGroupResponse = new PermissionGroupResponse();
                    permissionGroupResponse.setId(permissionGroup.getUuid());
                    permissionGroupResponse.setPermission_name(permissionGroup.getPermission_name());
                    permissionGroupResponse.setPermission_flag_object(permissionGroup.getPermission_flag_object());
                    listResponse.add(permissionGroupResponse);
                }
                String message = "Berhasil Mengambil Data Permission Group";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listPermissionGroup));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                listPermissionGroup = organizationId != null && !organizationId.toString().isEmpty()
                        ? permissionGroupRepository.findAllDataByOrgId(UUID.fromString(organizationId), pageable)
                        : permissionGroupRepository.findAllData(pageable);
                List<PermissionGroupResponse> listResponse = new ArrayList<>();
                for (PermissionGroup permissionGroup : listPermissionGroup) {

                    PermissionGroupResponse permissionGroupResponse = new PermissionGroupResponse();
                    permissionGroupResponse.setId(permissionGroup.getUuid());
                    permissionGroupResponse.setPermission_name(permissionGroup.getPermission_name());
                    permissionGroupResponse.setPermission_flag_object(permissionGroup.getPermission_flag_object());
                    listResponse.add(permissionGroupResponse);
                }
                String message = "Berhasil Mengambil Data Permission Group";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listPermissionGroup));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceUpdateData(UUID uuid,
            PermisionGroupRequest permisionGroupRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() &&
                    permisionGroupRequest.getPermission_name() != null &&
                    !permisionGroupRequest.getPermission_name().toString().isEmpty() &&
                    permisionGroupRequest.getPermission_flag_object() != null &&
                    !permisionGroupRequest.getPermission_flag_object().toString().isEmpty()) {
                Optional<PermissionGroup> optional = permissionGroupRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Permission Group";
                    Date currentDate = new Date();
                    PermissionGroup existingData = optional.get();
                    PermissionGroup updateData = mappingData(uuid_user_login, permisionGroupRequest,
                            existingData.getDate_created(), currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = permissionGroupRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);

                } else {
                    String message = "Data Permission Group Tidak Ditemukan";
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

    public ResponseEntity<LinkedHashMap<String, Object>> serviceDeleteData(UUID uuid) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty()) {
                Optional<PermissionGroup> optional = permissionGroupRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Data Permission Group Berhasil Dihapus";
                    permissionGroupRepository.delete(optional.get());
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Permission Group Tidak Ditemukan";
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
            Optional<PermissionGroup> optional = permissionGroupRepository.findByUuid(uuid);
            String permission_name = optional.map(PermissionGroup::getPermission_name).orElse("Unknown");

            String message = "Gagal menghapus " + permission_name
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

    private PermissionGroup mappingData(String uuid_user, PermisionGroupRequest permisionGroupRequest, Date createdDate,
            Date updatedDate) {
        PermissionGroup entity = new PermissionGroup();
        entity.setUuid(UUID.randomUUID());
        entity.setPermission_name(permisionGroupRequest.getPermission_name());
        entity.setPermission_flag_object(permisionGroupRequest.getPermission_flag_object());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }

    private LinkedHashMap<String, Object> getPageInfo(Page<PermissionGroup> listPermissionGroup) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("size", listPermissionGroup.getSize());
        response.put("totalItems", listPermissionGroup.getTotalElements());
        response.put("totalPages", listPermissionGroup.getTotalPages());
        response.put("currentPage", listPermissionGroup.getNumber());
        return response;
    }

}
