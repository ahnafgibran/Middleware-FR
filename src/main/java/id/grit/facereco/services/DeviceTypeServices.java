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
import id.grit.facereco.entity.DeviceType;
import id.grit.facereco.model.request.DeviceTypeRequest;
import id.grit.facereco.model.response.DeviceTypeResponse;
import id.grit.facereco.repository.DeviceTypeRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class DeviceTypeServices {

    private DeviceTypeRepository deviceTypeRepository;
    private JwtUtil jwtUtil;

    public DeviceTypeServices(DeviceTypeRepository deviceTypeRepository, JwtUtil jwtUtil) {
        this.deviceTypeRepository = deviceTypeRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(DeviceTypeRequest deviceTypeRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (deviceTypeRequest.getDevice_type_name() != null &&
                    !deviceTypeRequest.getDevice_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data Device Type";
                Date currentDate = new Date();
                deviceTypeRepository.save(mappingData(uuid_user_login, deviceTypeRequest, currentDate, currentDate));
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
            List<DeviceType> list = deviceTypeRepository.findAll();
            List<DeviceTypeResponse> listResponse = new ArrayList<>();

            for (DeviceType deviceType : list) {
                DeviceTypeResponse deviceTypeResponse = new DeviceTypeResponse();
                deviceTypeResponse.setId(deviceType.getUuid());
                deviceTypeResponse.setDevice_type_name(deviceType.getDevice_type_name());
                deviceTypeResponse.setFlag_active_device_type(deviceType.isFlag_active_device_type());
                listResponse.add(deviceTypeResponse);
            }
            String message = "Berhasil Mengambil Data Device Type";
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
            DeviceTypeRequest deviceTypeRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() && deviceTypeRequest.getDevice_type_name() != null &&
                    !deviceTypeRequest.getDevice_type_name().isEmpty() &&
                    uuid_user_login != null &&
                    !uuid_user_login.toString().isEmpty()) {
                Optional<DeviceType> optional = deviceTypeRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Device Type";
                    Date currentDate = new Date();
                    DeviceType existingData = optional.get();
                    DeviceType updateData = mappingData(uuid_user_login, deviceTypeRequest,
                            existingData.getDate_created(),
                            currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = deviceTypeRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Device Type Tidak Ditemukan";
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

    private DeviceType mappingData(String uuid_user, DeviceTypeRequest deviceTypeRequest, Date createdDate,
            Date updatedDate) {
        DeviceType entity = new DeviceType();
        entity.setUuid(UUID.randomUUID());
        entity.setDevice_type_name(deviceTypeRequest.getDevice_type_name());
        entity.setFlag_active_device_type(deviceTypeRequest.isFlag_active_device_type());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }
}
