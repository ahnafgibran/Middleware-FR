package id.grit.facereco.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.grit.facereco.config.JwtUtil;
import id.grit.facereco.entity.PersonFace;
import id.grit.facereco.entity.Report;
import id.grit.facereco.entity.ReportDetectedTarget;
import id.grit.facereco.entity.StreamingDevice;
import id.grit.facereco.model.request.ReportDetectedTargetReq;
import id.grit.facereco.model.response.PersonEngineRes;
import id.grit.facereco.model.response.ReportDetectedTargetRes;
import id.grit.facereco.model.response.ReportResponse;
import id.grit.facereco.model.response.StreamingDeviceResponse;
import id.grit.facereco.repository.PersonFaceRepository;
import id.grit.facereco.repository.ReportDetectedTargetRepository;
import id.grit.facereco.repository.ReportRepository;
import id.grit.facereco.repository.StreamingDeviceRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ReportDetectedTargetServices {
    private final ReportDetectedTargetRepository reportDetectedTargetRepository;
    private final ReportRepository reportRepository;
    private final StreamingDeviceRepository streamingDeviceRepository;
    private final PersonFaceRepository personFaceRepository;
    private final JwtUtil jwtUtil;

    public ReportDetectedTargetServices(
            ReportDetectedTargetRepository reportDetectedTargetRepository,
            ReportRepository reportRepository,
            StreamingDeviceRepository streamingDeviceRepository,
            PersonFaceRepository personFaceRepository, JwtUtil jwtUtil) {
        this.reportDetectedTargetRepository = reportDetectedTargetRepository;
        this.reportRepository = reportRepository;
        this.streamingDeviceRepository = streamingDeviceRepository;
        this.personFaceRepository = personFaceRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<Map<String, Object>> createReportDetectedTarget(ReportDetectedTargetReq request, HttpServletRequest httpRequest) {
    Map<String, Object> response = new HashMap<>();
    String userUuid = jwtUtil.getUuidFromToken(httpRequest);

    try {
        if (request.getReport_id() != null && !request.getReport_id().isEmpty() &&
            request.getReport_streaming_device_id() != null && !request.getReport_streaming_device_id().isEmpty() &&
            request.getIdentity_template_id() != null && !request.getIdentity_template_id().isEmpty() &&
            userUuid != null && !userUuid.isEmpty()) {

            UUID reportUuid = UUID.fromString(request.getReport_id());
            Report report = reportRepository.findByUuid(reportUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Report dengan UUID " + request.getReport_id() + " tidak ditemukan"));

            UUID streamingDeviceUuid = UUID.fromString(request.getReport_streaming_device_id());
            StreamingDevice streamingDevice = streamingDeviceRepository.findByUuid(streamingDeviceUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Streaming Device dengan UUID " + request.getReport_streaming_device_id() + " tidak ditemukan"));

            UUID personFaceUuid = UUID.fromString(request.getIdentity_template_id());
            PersonFace personFace = personFaceRepository.findByUuid(personFaceUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Person Face dengan UUID " + request.getIdentity_template_id() + " tidak ditemukan"));

            ReportDetectedTarget detectedTarget = new ReportDetectedTarget();
            detectedTarget.setUuid(UUID.randomUUID());
            detectedTarget.setReport_id(report);
            detectedTarget.setReport_streaming_device_id(streamingDevice);
            detectedTarget.setIdentity_template_id(personFace);
            detectedTarget.setLast_modified_by(UUID.fromString(userUuid));

            reportDetectedTargetRepository.save(detectedTarget);

            response.put("status", "SUKSES");
            response.put("message", "Berhasil menambahkan Report Detected Target");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } else {
            response.put("status", "ERROR");
            response.put("message", "Request tidak sesuai");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    } catch (IllegalArgumentException e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } catch (Exception e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
public ResponseEntity<Map<String, Object>> getReportDetectedTarget(UUID uuid) {
    Map<String, Object> response = new HashMap<>();

    try {
        ReportDetectedTarget detectedTarget = reportDetectedTargetRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Report Detected Target dengan UUID " + uuid + " tidak ditemukan"));

        Map<String, Object> data = new HashMap<>();
        data.put("id", detectedTarget.getUuid());
        data.put("report_id", detectedTarget.getReport_id().getUuid());
        data.put("report_streaming_device_id", detectedTarget.getReport_streaming_device_id().getUuid());
        data.put("identity_template_id", detectedTarget.getIdentity_template_id().getUuid());

        response.put("status", "SUKSES");
        response.put("message", "Berhasil mendapatkan Report Detected Target");
        response.put("data", data);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (IllegalArgumentException e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch (Exception e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

public ResponseEntity<Map<String, Object>> updateReportDetectedTarget(UUID uuid, ReportDetectedTargetReq request, HttpServletRequest httpRequest) {
    Map<String, Object> response = new HashMap<>();
    String userUuid = jwtUtil.getUuidFromToken(httpRequest);

    try {
        ReportDetectedTarget detectedTarget = reportDetectedTargetRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Report Detected Target dengan UUID " + uuid + " tidak ditemukan"));

        if (request.getReport_id() != null && !request.getReport_id().isEmpty()) {
            UUID reportUuid = UUID.fromString(request.getReport_id());
            Report report = reportRepository.findByUuid(reportUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Report dengan UUID " + request.getReport_id() + " tidak ditemukan"));
            detectedTarget.setReport_id(report);
        }

        if (request.getReport_streaming_device_id() != null && !request.getReport_streaming_device_id().isEmpty()) {
            UUID streamingDeviceUuid = UUID.fromString(request.getReport_streaming_device_id());
            StreamingDevice streamingDevice = streamingDeviceRepository.findByUuid(streamingDeviceUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Streaming Device dengan UUID " + request.getReport_streaming_device_id() + " tidak ditemukan"));
            detectedTarget.setReport_streaming_device_id(streamingDevice);
        }

        if (request.getIdentity_template_id() != null && !request.getIdentity_template_id().isEmpty()) {
            UUID personFaceUuid = UUID.fromString(request.getIdentity_template_id());
            PersonFace personFace = personFaceRepository.findByUuid(personFaceUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Person Face dengan UUID " + request.getIdentity_template_id() + " tidak ditemukan"));
            detectedTarget.setIdentity_template_id(personFace);
        }

        detectedTarget.setLast_modified_by(UUID.fromString(userUuid));

        reportDetectedTargetRepository.save(detectedTarget);

        response.put("status", "SUKSES");
        response.put("message", "Berhasil memperbarui Report Detected Target");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    } catch (IllegalArgumentException e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    } catch (Exception e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


public ResponseEntity<Map<String, Object>> deleteReportDetectedTarget(UUID uuid) {
    Map<String, Object> response = new HashMap<>();

    try {
        ReportDetectedTarget detectedTarget = reportDetectedTargetRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Report Detected Target dengan UUID " + uuid + " tidak ditemukan"));

        reportDetectedTargetRepository.delete(detectedTarget);

        response.put("status", "SUKSES");
        response.put("message", "Berhasil menghapus Report Detected Target");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (IllegalArgumentException e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    } catch (Exception e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


public ResponseEntity<Map<String, Object>> getAllReportDetectedTargets() {
    Map<String, Object> response = new HashMap<>();

    try {
        List<ReportDetectedTarget> detectedTargets = reportDetectedTargetRepository.findAll();

        // Konversi ke DTO
        List<ReportDetectedTargetRes> responses = detectedTargets.stream()
                .map(detectedTarget -> {
                    ReportDetectedTargetRes dto = new ReportDetectedTargetRes();
                    dto.setId(detectedTarget.getUuid());

                    // Detail Report
                    ReportResponse reportResponse = new ReportResponse();
                    if (detectedTarget.getReport_id() != null) {
                        Report report = detectedTarget.getReport_id();
                        reportResponse.setUuid(report.getUuid());
                        reportResponse.setVideo_file_name(report.getVideo_file_name());
                        reportResponse.setName_camera(report.getName_camera());
                        reportResponse.setReport_organization(report.getReport_organization_id() != null
                                ? report.getReport_organization_id().getOrganization_name()
                                : "Unknown Organization");
                        reportResponse.set_ip_camera(report.is_ip_camera());
                        reportResponse.set_bwc(report.is_bwc());
                    }
                    dto.setReport_id(reportResponse);

                    // Detail Streaming Device
                    StreamingDeviceResponse streamingDeviceResponse = new StreamingDeviceResponse();
                    if (detectedTarget.getReport_streaming_device_id() != null) {
                        StreamingDevice device = detectedTarget.getReport_streaming_device_id();
                        streamingDeviceResponse.setId(device.getUuid());
                        streamingDeviceResponse.setDevice_name(device.getDevice_name());
                        streamingDeviceResponse.setIp_address(device.getIp_address());
                        streamingDeviceResponse.setDevice_url_rtsp(device.getDevice_url_rtsp());
                        // streamingDeviceResponse.setDevice_organization_id(device.getDevice_organization_id() != null
                        //         ? device.getDevice_organization_id().getOrganization_name()
                        //         : "Unknown Organization");
                        // streamingDeviceResponse.setDevice_type_id(device.getDevice_type_id() != null
                        //         ? device.getDevice_type_id().getDevice_type_name()
                        //         : "Unknown Device Type");
                        // streamingDeviceResponse.setOperation_id(device.getOperation_id() != null
                        //         ? device.getOperation_id().getOperation_name()
                        //         : "Unknown Operation");
                    }
                    dto.setReport_streaming_device_id(streamingDeviceResponse);

                    // Detail Person Face
                    PersonEngineRes personFaceResponse = new PersonEngineRes();
                    if (detectedTarget.getIdentity_template_id() != null) {
                        PersonFace person = detectedTarget.getIdentity_template_id();
                        personFaceResponse.setUuid(person.getUuid());
                        personFaceResponse.setNik(person.getNik());
                        personFaceResponse.setFullName(person.getFullName());
                        personFaceResponse.setGenderName(person.getGender().getGender_name());
                        personFaceResponse.setPlaceOfBirth(person.getPlaceOfBirth());
                        personFaceResponse.setDateOfBirth(person.getDateOfBirth());
                        personFaceResponse.setBloodTypeName(person.getBloodType().getBlood_type_name());
                        personFaceResponse.setAddress(person.getAddress());
                        personFaceResponse.setReligionName(person.getReligion().getReligion_type_name());
                        personFaceResponse.setMaritalStatus(person.getMaritalType().getMarital_type_name());
                        personFaceResponse.setOccupation(person.getOccupation());
                        personFaceResponse.setNationalityName(person.getNationality().getNationality_type_name());
                        personFaceResponse.setImage(person.getImage());
                        personFaceResponse.setDateCreated(person.getDate_created());
                        personFaceResponse.setDateModified(person.getDate_modified());
                        personFaceResponse.setLastModifiedBy(person.getLast_modified_by());
                        personFaceResponse.setDpo(person.isDpo());
                    }
                    dto.setIdentity_template_id(personFaceResponse);

                    return dto;
                }).toList();

        response.put("status", "SUKSES");
        response.put("message", "Berhasil mengambil data Report Detected Target");
        response.put("data", responses);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}






}
