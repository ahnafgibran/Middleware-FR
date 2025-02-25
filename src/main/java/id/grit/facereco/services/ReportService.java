package id.grit.facereco.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import id.grit.facereco.config.JwtUtil;
import id.grit.facereco.entity.Organization;
import id.grit.facereco.entity.Report;
import id.grit.facereco.model.request.ReportRequest;
import id.grit.facereco.model.response.ReportResponse;
import id.grit.facereco.repository.OrganizationRepository;
import id.grit.facereco.repository.ReportRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final OrganizationRepository organizationRepository;
    private final JwtUtil jwtUtil;

    public ReportService(ReportRepository reportRepository, JwtUtil jwtUtil, OrganizationRepository organizationRepository) {
        this.reportRepository = reportRepository;
        this.organizationRepository = organizationRepository;
        this.jwtUtil = jwtUtil;
    }

   public ResponseEntity<Map<String, Object>> createReport(ReportRequest request, HttpServletRequest httpRequest) {
    Map<String, Object> response = new HashMap<>();
    String userUuid = jwtUtil.getUuidFromToken(httpRequest);

    try {
        if (request.getVideo_file_name() != null && !request.getVideo_file_name().isEmpty() &&
            request.getName_camera() != null && !request.getName_camera().isEmpty() &&
            request.getReport_organization_id() != null && !request.getReport_organization_id().isEmpty() &&
            userUuid != null && !userUuid.isEmpty()) {

            UUID organizationUuid = UUID.fromString(request.getReport_organization_id());
            Organization organization = organizationRepository.findByUuid(organizationUuid)
                    .orElseThrow(() -> new IllegalArgumentException("Organization dengan UUID " + request.getReport_organization_id() + " tidak ditemukan"));

            Report report = new Report();
            report.setUuid(UUID.randomUUID());
            report.setVideo_file_name(request.getVideo_file_name());
            report.setName_camera(request.getName_camera());
            report.set_bwc(request.is_bwc());
            report.set_ip_camera(request.is_ip_camera());
            report.setLast_modified_by(UUID.fromString(userUuid));
            report.setReport_organization_id(organization);

            reportRepository.save(report);

            response.put("status", "SUKSES");
            response.put("message", "Berhasil menambahkan report");
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


    public ResponseEntity<Map<String, Object>> getReports() {
    Map<String, Object> response = new HashMap<>();

    try {
        List<Report> reports = reportRepository.findAll();

        // Iterasi langsung untuk membangun ReportResponse
        List<ReportResponse> reportResponses = new ArrayList<>();
        for (Report report : reports) {
            ReportResponse reportResponse = new ReportResponse();
            reportResponse.setUuid(report.getUuid());
            reportResponse.setVideo_file_name(report.getVideo_file_name());
            reportResponse.setName_camera(report.getName_camera());
            reportResponse.set_ip_camera(report.is_ip_camera());
            reportResponse.set_bwc(report.is_bwc());
            reportResponse.setReport_organization(report.getReport_organization_id().getOrganization_name());

            reportResponses.add(reportResponse);
        }

        response.put("status", "SUKSES");
        response.put("message", "Berhasil mengambil data report");
        response.put("data", reportResponses);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
        response.put("status", "ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}


public ResponseEntity<Map<String, Object>> updateReport(UUID uuid, ReportRequest request, HttpServletRequest httpRequest) {
    Map<String, Object> response = new HashMap<>();
    String userUuid = jwtUtil.getUuidFromToken(httpRequest);

    try {
        Optional<Report> optionalReport = reportRepository.findByUuid(uuid);
        if (optionalReport.isPresent() && userUuid != null && !userUuid.isEmpty()) {
            Report report = optionalReport.get();

            if (request.getReport_organization_id() != null && !request.getReport_organization_id().isEmpty()) {
                UUID organizationUuid = UUID.fromString(request.getReport_organization_id());
                Organization organization = organizationRepository.findByUuid(organizationUuid)
                        .orElseThrow(() -> new IllegalArgumentException("Organization dengan UUID " + request.getReport_organization_id() + " tidak ditemukan"));
                report.setReport_organization_id(organization);
            }

            report.setVideo_file_name(request.getVideo_file_name());
            report.setName_camera(request.getName_camera());
            report.set_ip_camera(request.is_ip_camera());
            report.set_bwc(request.is_bwc());
            report.setLast_modified_by(UUID.fromString(userUuid));

            reportRepository.save(report);

            response.put("status", "SUKSES");
            response.put("message", "Berhasil mengupdate report");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("status", "ERROR");
            response.put("message", "Report tidak ditemukan");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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


    public ResponseEntity<Map<String, Object>> deleteReport(UUID uuid) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Report> optionalReport = reportRepository.findByUuid(uuid);
            if (optionalReport.isPresent()) {
                reportRepository.delete(optionalReport.get());
                response.put("status", "SUKSES");
                response.put("message", "Berhasil menghapus report");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("status", "ERROR");
                response.put("message", "Report tidak ditemukan");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
