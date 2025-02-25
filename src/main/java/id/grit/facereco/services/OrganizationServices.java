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
import id.grit.facereco.entity.JurisdictionLevel;
import id.grit.facereco.entity.Organization;
import id.grit.facereco.model.request.OrganizationRequest;
import id.grit.facereco.model.response.OrganizationResponse;
import id.grit.facereco.repository.JurisdictionRepository;
import id.grit.facereco.repository.OrganizationRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;

@Service
public class OrganizationServices {

    private OrganizationRepository organizationRepository;
    private JurisdictionRepository jurisdictionRepository;
    private JwtUtil jwtUtil;

    public OrganizationServices(JurisdictionRepository jurisdictionRepository,
            OrganizationRepository organizationRepository, JwtUtil jwtUtil) {
        this.jurisdictionRepository = jurisdictionRepository;
        this.organizationRepository = organizationRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<LinkedHashMap<String, Object>> serviceCreateData(OrganizationRequest organizationRequest,
            HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (organizationRequest.getOrganization_photo() != null &&
                    !organizationRequest.getOrganization_photo().toString().isEmpty() &&
                    organizationRequest.getOrganization_name() != null &&
                    !organizationRequest.getOrganization_name().toString().isEmpty() &&
                    organizationRequest.getOrganization_code() != null &&
                    !organizationRequest.getOrganization_code().toString().isEmpty() &&
                    organizationRequest.getOrganization_office_address() != null &&
                    !organizationRequest.getOrganization_office_address().toString().isEmpty() &&
                    organizationRequest.getOrganization_office_telephone() != null &&
                    !organizationRequest.getOrganization_office_telephone().toString().isEmpty() &&
                    organizationRequest.getOrganization_email() != null &&
                    !organizationRequest.getOrganization_email().toString().isEmpty() &&
                    organizationRequest.getOrganization_date_establishment() != null &&
                    !organizationRequest.getOrganization_date_establishment().toString().isEmpty() &&
                    organizationRequest.getOrganization_jurisdiction_level_id() != null &&
                    !organizationRequest.getOrganization_jurisdiction_level_id().toString().isEmpty() &&
                    organizationRequest.getOrganization_total_personnel() != null &&
                    !organizationRequest.getOrganization_total_personnel().toString().isEmpty() &&
                    organizationRequest.getOrganization_parent_id() != null &&
                    !organizationRequest.getOrganization_parent_id().toString().isEmpty()) {
                String message = "Berhasil Menambahkan Data Organization";
                Date currentDate = new Date();
                organizationRepository
                        .save(mappingData(uuid_user_login, organizationRequest, currentDate, currentDate));
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

    public ResponseEntity<LinkedHashMap<String, Object>> serviceGetDataOrganization(String keywordPencarian,
            Integer page,
            Integer size) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Organization> listOrganization = null;
            if (keywordPencarian != null && !keywordPencarian.toString().isEmpty()) {
                listOrganization = organizationRepository.findByOrganizationDataList(keywordPencarian, pageable);
                List<OrganizationResponse> listResponse = new ArrayList<>();
                for (Organization organization : listOrganization) {
                    OrganizationResponse organizationResponse = new OrganizationResponse();

                    JurisdictionLevel jurisdictionLevel = jurisdictionRepository
                            .findByUuid(organization.getOrganization_jurisdiction_level_id())
                            .orElseThrow(() -> new RuntimeException("Organization not found"));

                    organizationResponse.setId(organization.getUuid());
                    organizationResponse.setOrganization_photo("");
                    organizationResponse.setOrganization_name(organization.getOrganization_name());
                    organizationResponse.setOrganization_code(organization.getOrganization_code());
                    organizationResponse.setOrganization_office_address(organization.getOrganization_office_address());
                    organizationResponse.setOrganization_email(organization.getOrganization_office_email());
                    organizationResponse
                            .setOrganization_office_telephone(organization.getOrganization_office_telephone());
                    organizationResponse
                            .setOrganization_date_establishment(organization.getOrganization_date_establishment());
                    organizationResponse.setOrganization_jurisdiction_level_id(
                            organization.getOrganization_jurisdiction_level_id());
                    organizationResponse
                            .setOrganization_total_personnel(organization.getOrganization_total_personnel());
                    organizationResponse.setOrganization_parent_id(organization.getOrganization_parent_id());
                    organizationResponse
                            .setOrganization_jurisdiction_level_code(jurisdictionLevel.getJurisdiction_code());
                    listResponse.add(organizationResponse);
                }
                String message = "Berhasil Mengambil Data Organization";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listOrganization));
                return ResponseEntity.status(HttpStatus.OK).body(response);

            } else {
                listOrganization = organizationRepository.findAll(pageable);
                List<OrganizationResponse> listResponse = new ArrayList<>();
                for (Organization organization : listOrganization) {
                    OrganizationResponse organizationResponse = new OrganizationResponse();
                    JurisdictionLevel jurisdictionLevel = jurisdictionRepository
                            .findByUuid(organization.getOrganization_jurisdiction_level_id())
                            .orElseThrow(() -> new RuntimeException("Organization not found"));
                    organizationResponse.setId(organization.getUuid());
                    organizationResponse.setOrganization_photo(organization.getOrganization_photo());
                    organizationResponse.setOrganization_name(organization.getOrganization_name());
                    organizationResponse.setOrganization_code(organization.getOrganization_code());
                    organizationResponse.setOrganization_office_address(organization.getOrganization_office_address());
                    organizationResponse.setOrganization_email(organization.getOrganization_office_email());
                    organizationResponse
                            .setOrganization_office_telephone(organization.getOrganization_office_telephone());
                    organizationResponse
                            .setOrganization_date_establishment(organization.getOrganization_date_establishment());
                    organizationResponse.setOrganization_jurisdiction_level_id(
                            organization.getOrganization_jurisdiction_level_id());
                    organizationResponse
                            .setOrganization_total_personnel(organization.getOrganization_total_personnel());
                    organizationResponse.setOrganization_parent_id(organization.getOrganization_parent_id());
                    organizationResponse
                            .setOrganization_jurisdiction_level_code(jurisdictionLevel.getJurisdiction_code());
                    listResponse.add(organizationResponse);
                }
                String message = "Berhasil Mengambil Data Organization";
                response.put("status", "SUKSES");
                response.put("message", message);
                response.put("data", listResponse);
                response.putAll(getPageInfo(listOrganization));
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
                Optional<Organization> optional = organizationRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Data Organization Berhasil Dihapus";
                    organizationRepository.delete(optional.get());
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Organization Tidak Ditemukan";
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
            Optional<Organization> optional = organizationRepository.findByUuid(uuid);
            String organisasi_name = optional.map(Organization::getOrganization_name).orElse("Unknown");

            String message = "Gagal menghapus " + organisasi_name
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
            OrganizationRequest organizationRequest, HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String uuid_user_login = jwtUtil.getUuidFromToken(request);
        try {
            if (uuid != null &&
                    !uuid.toString().isEmpty() &&
                    organizationRequest.getOrganization_photo() != null &&
                    !organizationRequest.getOrganization_photo().toString().isEmpty() &&
                    organizationRequest.getOrganization_name() != null &&
                    !organizationRequest.getOrganization_name().toString().isEmpty() &&
                    organizationRequest.getOrganization_code() != null &&
                    !organizationRequest.getOrganization_code().toString().isEmpty() &&
                    organizationRequest.getOrganization_office_address() != null &&
                    !organizationRequest.getOrganization_office_address().toString().isEmpty() &&
                    organizationRequest.getOrganization_office_telephone() != null &&
                    !organizationRequest.getOrganization_office_telephone().toString().isEmpty() &&
                    organizationRequest.getOrganization_email() != null &&
                    !organizationRequest.getOrganization_email().toString().isEmpty() &&
                    organizationRequest.getOrganization_date_establishment() != null &&
                    !organizationRequest.getOrganization_date_establishment().toString().isEmpty() &&
                    organizationRequest.getOrganization_jurisdiction_level_id() != null &&
                    !organizationRequest.getOrganization_jurisdiction_level_id().toString().isEmpty() &&
                    organizationRequest.getOrganization_total_personnel() != null &&
                    !organizationRequest.getOrganization_total_personnel().toString().isEmpty() &&
                    organizationRequest.getOrganization_parent_id() != null &&
                    !organizationRequest.getOrganization_parent_id().toString().isEmpty()) {
                Optional<Organization> optional = organizationRepository.findByUuid(uuid);
                if (optional.isPresent()) {
                    String message = "Berhasil Mengubah Data Organization";
                    Date currentDate = new Date();
                    Organization existingData = optional.get();
                    Organization updateData = mappingData(uuid_user_login, organizationRequest,
                            existingData.getDate_created(), currentDate);

                    // Set Value
                    updateData.setId(existingData.getId());
                    updateData.setUuid(uuid);

                    // save the update entity
                    updateData = organizationRepository.save(updateData);
                    response.put("status", "SUKSES");
                    response.put("message", message);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    String message = "Data Organization Tidak Ditemukan";
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

    private Organization mappingData(String uuid_user, OrganizationRequest organizationRequest, Date createdDate,
            Date updatedDate) {
        Organization entity = new Organization();
        entity.setUuid(UUID.randomUUID());
        entity.setOrganization_photo(organizationRequest.getOrganization_photo());
        entity.setOrganization_name(organizationRequest.getOrganization_name());
        entity.setOrganization_code(organizationRequest.getOrganization_code());
        entity.setOrganization_office_address(organizationRequest.getOrganization_office_address());
        entity.setOrganization_office_telephone(organizationRequest.getOrganization_office_telephone());
        entity.setOrganization_office_email(organizationRequest.getOrganization_email());
        entity.setOrganization_date_establishment(organizationRequest.getOrganization_date_establishment());
        entity.setOrganization_jurisdiction_level_id(organizationRequest.getOrganization_jurisdiction_level_id());
        entity.setOrganization_parent_id(organizationRequest.getOrganization_parent_id());
        entity.setOrganization_total_personnel(organizationRequest.getOrganization_total_personnel());
        entity.setDate_created(createdDate);
        entity.setDate_modified(updatedDate);
        entity.setLast_modified_by(UUID.fromString(uuid_user));
        return entity;
    }

    private LinkedHashMap<String, Object> getPageInfo(Page<Organization> listOrganization) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("size", listOrganization.getSize());
        response.put("totalItems", listOrganization.getTotalElements());
        response.put("totalPages", listOrganization.getTotalPages());
        response.put("currentPage", listOrganization.getNumber());
        return response;
    }
}
