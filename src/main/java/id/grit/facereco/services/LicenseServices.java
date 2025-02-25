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
import id.grit.facereco.entity.License;
import id.grit.facereco.model.request.LicenseRequest;
import id.grit.facereco.model.response.LicenseResponse;
import id.grit.facereco.repository.LicenseRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class LicenseServices {

    private LicenseRepository licenseRepository;
    private JwtUtil jwtUtil;

    public LicenseServices(LicenseRepository licenseRepository, JwtUtil jwtUtil) {
        this.licenseRepository = licenseRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(LicenseRequest licenseRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (licenseRequest.getLicense_name() != null &&
                    !licenseRequest.getLicense_name().toString().isEmpty() &&
                    licenseRequest.getLicense_serial_number() != null &&
                    !licenseRequest.getLicense_serial_number().toString().isEmpty() &&
                    licenseRequest.getLicense_description() != null &&
                    !licenseRequest.getLicense_description().toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data License";
                Date currentDate = new Date();
                licenseRepository
                        .save(mappingData(uuid_user_login, licenseRequest, currentDate, currentDate));
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

    public ResponseEntity<LinkedHashMap<String, Object>> serviceGetDataLicense(String keywordPencarian,
            Integer page,
            Integer size) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<License> listLicense = null;
            if (keywordPencarian != null && !keywordPencarian.toString().isEmpty()) {
                listLicense = licenseRepository.findByLicenseDataList(keywordPencarian, pageable);
                List<LicenseResponse> listResponse = new ArrayList<>();
                for (License license : listLicense) {
                    LicenseResponse licenseResponse = new LicenseResponse();
                    licenseResponse.setId(license.getUuid());
                    licenseResponse.setLicense_name(license.getLicense_name());
                    licenseResponse.setLicense_description(license.getLicense_description());
                    licenseResponse.setLicense_serial_number(license.getLicense_serial_number());
                    licenseResponse.setLicense_activated_date(license.getLicense_activated_date());
                    licenseResponse.setLicense_expired_date(license.getLicense_expired_date());
                    listResponse.add(licenseResponse);
                }
                String message = "Berhasil Mengambil Data License";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listLicense));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                listLicense = licenseRepository.findAll(pageable);
                List<LicenseResponse> listResponse = new ArrayList<>();
                for (License license : listLicense) {
                    LicenseResponse licenseResponse = new LicenseResponse();
                    licenseResponse.setId(license.getUuid());
                    licenseResponse.setLicense_name(license.getLicense_name());
                    licenseResponse.setLicense_description(license.getLicense_description());
                    licenseResponse.setLicense_serial_number(license.getLicense_serial_number());
                    licenseResponse.setLicense_activated_date(license.getLicense_activated_date());
                    licenseResponse.setLicense_expired_date(license.getLicense_expired_date());
                    listResponse.add(licenseResponse);
                }
                String message = "Berhasil Mengambil Data License";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listLicense));
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceUpdateData(UUID uuid, LicenseRequest licenseRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() &&
                    licenseRequest.getLicense_name() != null &&
                    !licenseRequest.getLicense_name().toString().isEmpty() &&
                    licenseRequest.getLicense_serial_number() != null &&
                    !licenseRequest.getLicense_serial_number().toString().isEmpty() &&
                    licenseRequest.getLicense_description() != null &&
                    !licenseRequest.getLicense_description().toString().isEmpty()) {
                Optional<License> optional = licenseRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data License";
                    Date currentDate = new Date();
                    License existingData = optional.get();
                    License updateData = mappingData(uuid_user_login, licenseRequest,
                            existingData.getDate_created(), currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = licenseRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data License Tidak Ditemukan";
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
                    uuid.toString().isEmpty()) {
                Optional<License> optional = licenseRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Data License Berhasil Dihapus";
                    licenseRepository.deleteByUuid(uuid);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data License Tidak Ditemukan";
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
            Optional<License> optional = licenseRepository.findByUuid(uuid);
            String license_name = optional.map(License::getLicense_name).orElse("Unknown");

            String message = "Gagal menghapus " + license_name
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

    private License mappingData(String uuid_user, LicenseRequest licenseRequest, Date createdDate,
            Date updatedDate) {
        License entity = new License();
        entity.setUuid(UUID.randomUUID());
        entity.setLicense_name(licenseRequest.getLicense_name());
        entity.setLicense_description(licenseRequest.getLicense_description());
        entity.setLicense_serial_number(licenseRequest.getLicense_serial_number());
        entity.setLicense_activated_date(createdDate);
        entity.setLicense_expired_date(null);
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }

    private LinkedHashMap<String, Object> getPageInfo(Page<License> listOperation) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("size", listOperation.getSize());
        response.put("totalItems", listOperation.getTotalElements());
        response.put("totalPages", listOperation.getTotalPages());
        response.put("currentPage", listOperation.getNumber());
        return response;
    }
}
