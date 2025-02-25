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
import id.grit.facereco.entity.DeviceType;
import id.grit.facereco.entity.Operation;
import id.grit.facereco.entity.Organization;
import id.grit.facereco.entity.StreamingDevice;
import id.grit.facereco.model.request.StreamingDeviceRequest;
import id.grit.facereco.model.response.StreamingDeviceResponse;
import id.grit.facereco.repository.DeviceTypeRepository;
import id.grit.facereco.repository.OperationRepository;
import id.grit.facereco.repository.OrganizationRepository;
import id.grit.facereco.repository.StreamingDeviceRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class StreamingDevicesServices {

    private StreamingDeviceRepository streamingDeviceRepository;
    private OrganizationRepository organizationRepository;
    private DeviceTypeRepository deviceTypeRepository;
    private OperationRepository operationRepository;
    private JwtUtil jwtUtil;

    public StreamingDevicesServices(StreamingDeviceRepository streamingDeviceRepository,
            OrganizationRepository organizationRepository, DeviceTypeRepository deviceTypeRepository,
            OperationRepository operationRepository, JwtUtil jwtUtil) {
        this.operationRepository = operationRepository;
        this.deviceTypeRepository = deviceTypeRepository;
        this.organizationRepository = organizationRepository;
        this.streamingDeviceRepository = streamingDeviceRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(
            StreamingDeviceRequest streamingDeviceRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (streamingDeviceRequest.getDevice_name() != null &&
                    !streamingDeviceRequest.getDevice_name().toString().isEmpty() &&
                    streamingDeviceRequest.getIp_address() != null &&
                    !streamingDeviceRequest.getIp_address().toString().isEmpty() &&
                    streamingDeviceRequest.getDevice_organization_id() != null &&
                    !streamingDeviceRequest.getDevice_organization_id().toString().isEmpty() &&
                    streamingDeviceRequest.getDevice_type_id() != null &&
                    !streamingDeviceRequest.getDevice_type_id().toString().isEmpty() &&
                    streamingDeviceRequest.getDevice_url_rtsp() != null &&
                    !streamingDeviceRequest.getDevice_url_rtsp().toString().isEmpty()) {
                Optional<Organization> organization = organizationRepository
                        .findByUuid(streamingDeviceRequest.getDevice_organization_id());

                Optional<DeviceType> deviceType = deviceTypeRepository
                        .findByUuid(streamingDeviceRequest.getDevice_type_id());

                if (organization.isEmpty()) {
                    String message = "Organization tidak ditemukan";
                    response.put("status", "ERROR");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                } else if (deviceType.isEmpty()) {
                    String message = "Tipe Perangkat tidak ditemukan";
                    response.put("status", "ERROR");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                } else {
                    if (streamingDeviceRequest.getOperation_id() != null
                            && !streamingDeviceRequest.getOperation_id().toString().isEmpty()) {
                        Optional<Operation> operation = operationRepository
                                .findByUuid(streamingDeviceRequest.getOperation_id());
                        if (operation.isPresent()) {
                            String message = "Berhasil Menambahkan Data Streaming Device";
                            Date currentDate = new Date();
                            streamingDeviceRepository
                                    .save(mappingData(uuid_user_login, streamingDeviceRequest, currentDate,
                                            currentDate));
                            response.put("status", "SUKSES");
                            response.put("message", message);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        } else {
                            String message = "Operasi tidak ditemukan";
                            response.put("status", "ERROR");
                            response.put("message", message);
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }
                    } else {
                        String message = "Berhasil Menambahkan Data Streaming Device";
                        Date currentDate = new Date();
                        streamingDeviceRequest.setOperation_id(null);
                        streamingDeviceRepository
                                .save(mappingData(uuid_user_login, streamingDeviceRequest, currentDate,
                                        currentDate));
                        response.put("status", "SUKSES");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.OK).body(response);
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

    public ResponseEntity<LinkedHashMap<String, Object>> serviceGetDataStreamingDevice(String keywordPencarian,
            String organizationId,
            Integer page,
            Integer size) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<StreamingDevice> listStreamingDevice = null;
            if (keywordPencarian != null && !keywordPencarian.toString().isEmpty()) {
                listStreamingDevice = organizationId != null && !organizationId.toString().isEmpty()
                        ? streamingDeviceRepository.findDataByKeywordAndOrgId(keywordPencarian,
                                UUID.fromString(organizationId),
                                pageable)
                        : streamingDeviceRepository.findDataByKeyword(keywordPencarian, pageable);
                List<StreamingDeviceResponse> listResponse = new ArrayList<>();
                for (StreamingDevice streamingDevice : listStreamingDevice) {
                    Optional<Organization> organization = organizationRepository
                            .findByUuid(streamingDevice.getDevice_organization_id());
                    Optional<DeviceType> deviceType = deviceTypeRepository
                            .findByUuid(streamingDevice.getDevice_type_id());
                    Optional<Operation> operation = operationRepository
                            .findByUuid(streamingDevice.getDevice_operation_id());
                    StreamingDeviceResponse streamingDeviceResponse = new StreamingDeviceResponse();
                    streamingDeviceResponse.setId(streamingDevice.getUuid());
                    streamingDeviceResponse.setDevice_name(streamingDevice.getDevice_name());
                    streamingDeviceResponse.setIp_address(streamingDevice.getIp_address());
                    streamingDeviceResponse.setDevice_url_rtsp(streamingDevice.getDevice_url_rtsp());
                    streamingDeviceResponse.setDevice_organization_id(streamingDevice.getDevice_organization_id());
                    streamingDeviceResponse.setDevice_type_id(streamingDevice.getDevice_type_id());
                    streamingDeviceResponse.setOperation_id(streamingDevice.getDevice_operation_id());
                    streamingDeviceResponse.setOrganization_name(
                            organization.isPresent() ? organization.get().getOrganization_name() : "-");
                    streamingDeviceResponse
                            .setTipe_perangkat(deviceType.isPresent() ? deviceType.get().getDevice_type_name() : "-");
                    streamingDeviceResponse
                            .setOperation_name(operation.isPresent() ? operation.get().getOperation_name() : "-");
                    streamingDeviceResponse.setLatitude_device(streamingDevice.getLatitude_device());
                    streamingDeviceResponse.setLongitude_device(streamingDevice.getLongitude_device());
                    listResponse.add(streamingDeviceResponse);
                }
                String message = "Berhasil Mengambil Data Streaming Device";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listStreamingDevice));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                listStreamingDevice = organizationId != null && !organizationId.toString().isEmpty()
                        ? streamingDeviceRepository.findAllDataByOrgId(UUID.fromString(organizationId), pageable)
                        : streamingDeviceRepository.findAllData(pageable);
                List<StreamingDeviceResponse> listResponse = new ArrayList<>();
                for (StreamingDevice streamingDevice : listStreamingDevice) {
                    Optional<Organization> organization = organizationRepository
                            .findByUuid(streamingDevice.getDevice_organization_id());
                    Optional<DeviceType> deviceType = deviceTypeRepository
                            .findByUuid(streamingDevice.getDevice_type_id());
                    Optional<Operation> operation = operationRepository
                            .findByUuid(streamingDevice.getDevice_operation_id());
                    StreamingDeviceResponse streamingDeviceResponse = new StreamingDeviceResponse();
                    streamingDeviceResponse.setId(streamingDevice.getUuid());
                    streamingDeviceResponse.setDevice_name(streamingDevice.getDevice_name());
                    streamingDeviceResponse.setIp_address(streamingDevice.getIp_address());
                    streamingDeviceResponse.setDevice_url_rtsp(streamingDevice.getDevice_url_rtsp());
                    streamingDeviceResponse.setDevice_organization_id(streamingDevice.getDevice_organization_id());
                    streamingDeviceResponse.setDevice_type_id(streamingDevice.getDevice_type_id());
                    streamingDeviceResponse.setOperation_id(streamingDevice.getDevice_operation_id());
                    streamingDeviceResponse.setOrganization_name(
                            organization.isPresent() ? organization.get().getOrganization_name() : "-");
                    streamingDeviceResponse
                            .setTipe_perangkat(deviceType.isPresent() ? deviceType.get().getDevice_type_name() : "-");
                    streamingDeviceResponse
                            .setOperation_name(operation.isPresent() ? operation.get().getOperation_name() : "-");
                    streamingDeviceResponse.setLatitude_device(streamingDevice.getLatitude_device());
                    streamingDeviceResponse.setLongitude_device(streamingDevice.getLongitude_device());
                    listResponse.add(streamingDeviceResponse);
                }
                String message = "Berhasil Mengambil Data Streaming Device";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listStreamingDevice));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceUpdateData(UUID uuid,
            StreamingDeviceRequest streamingDeviceRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() &&
                    streamingDeviceRequest.getDevice_name() != null &&
                    !streamingDeviceRequest.getDevice_name().toString().isEmpty() &&
                    streamingDeviceRequest.getIp_address() != null &&
                    !streamingDeviceRequest.getIp_address().toString().isEmpty() &&
                    streamingDeviceRequest.getDevice_organization_id() != null &&
                    !streamingDeviceRequest.getDevice_organization_id().toString().isEmpty() &&
                    streamingDeviceRequest.getDevice_type_id() != null &&
                    !streamingDeviceRequest.getDevice_type_id().toString().isEmpty() &&
                    streamingDeviceRequest.getDevice_url_rtsp() != null &&
                    !streamingDeviceRequest.getDevice_url_rtsp().toString().isEmpty()) {
                Optional<StreamingDevice> optional = streamingDeviceRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    Optional<Organization> organization = organizationRepository
                            .findByUuid(streamingDeviceRequest.getDevice_organization_id());
                    Optional<DeviceType> deviceType = deviceTypeRepository
                            .findByUuid(streamingDeviceRequest.getDevice_type_id());
                    if (organization.isEmpty()) {
                        String message = "Organization tidak ditemukan";
                        response.put("status", "ERROR");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    } else if (deviceType.isEmpty()) {
                        String message = "Tipe Perangkat tidak ditemukan";
                        response.put("status", "ERROR");
                        response.put("message", message);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    } else {
                        if (streamingDeviceRequest.getOperation_id() != null
                                && !streamingDeviceRequest.getOperation_id().toString().isEmpty()) {
                            Optional<Operation> operation = operationRepository
                                    .findByUuid(streamingDeviceRequest.getOperation_id());
                            if (operation.isPresent()) {
                                String message = "Berhasil Mengubah Data Streaming Device";
                                Date currentDate = new Date();
                                StreamingDevice existingData = optional.get();
                                StreamingDevice updateData = mappingData(uuid_user_login, streamingDeviceRequest,
                                        existingData.getDate_created(), currentDate);

                                // Set Value
                                updateData.setId(existingData.getId());
                                updateData.setUuid(uuid);

                                // save the update entity
                                updateData = streamingDeviceRepository.save(updateData);
                                response.put("status", "SUKSES");
                                response.put("message", message);
                                return ResponseEntity.status(HttpStatus.OK).body(response);
                            } else {
                                String message = "Operasi tidak ditemukan";
                                response.put("status", "ERROR");
                                response.put("message", message);
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                            }
                        } else {
                            String message = "Berhasil Mengubah Data Streaming Device";
                            Date currentDate = new Date();
                            StreamingDevice existingData = optional.get();
                            streamingDeviceRequest.setOperation_id(null);
                            StreamingDevice updateData = mappingData(uuid_user_login, streamingDeviceRequest,
                                    existingData.getDate_created(), currentDate);

                            // Set Value
                            updateData.setId(existingData.getId());
                            updateData.setUuid(uuid);

                            // save the update entity
                            updateData = streamingDeviceRepository.save(updateData);
                            response.put("status", "SUKSES");
                            response.put("message", message);
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                        }
                    }
                } else {
                    String message = "Data Streaming Device Tidak Ditemukan";
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
                Optional<StreamingDevice> optional = streamingDeviceRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Data Streaming Device Berhasil Dihapus";
                    streamingDeviceRepository.delete(optional.get());
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Streaming Device Tidak Ditemukan";
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
            Optional<StreamingDevice> optional = streamingDeviceRepository.findByUuid(uuid);
            String device_name = optional.map(StreamingDevice::getDevice_name).orElse("Unknown");

            String message = "Gagal menghapus " + device_name
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

    private StreamingDevice mappingData(String uuid_user, StreamingDeviceRequest streamingDeviceRequest,
            Date createdDate,
            Date updatedDate) {
        StreamingDevice entity = new StreamingDevice();
        entity.setUuid(UUID.randomUUID());
        entity.setDevice_name(streamingDeviceRequest.getDevice_name());
        entity.setIp_address(streamingDeviceRequest.getIp_address());
        entity.setDevice_url_rtsp(streamingDeviceRequest.getDevice_url_rtsp());
        entity.setDevice_organization_id(streamingDeviceRequest.getDevice_organization_id());
        entity.setDevice_type_id(streamingDeviceRequest.getDevice_type_id());
        entity.setDevice_operation_id(streamingDeviceRequest.getOperation_id());
        entity.setLatitude_device(streamingDeviceRequest.getLatitude_device());
        entity.setLongitude_device(streamingDeviceRequest.getLongitude_device());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }

    private LinkedHashMap<String, Object> getPageInfo(Page<StreamingDevice> listStreamingDevice) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("size", listStreamingDevice.getSize());
        response.put("totalItems", listStreamingDevice.getTotalElements());
        response.put("totalPages", listStreamingDevice.getTotalPages());
        response.put("currentPage", listStreamingDevice.getNumber());
        return response;
    }

}
