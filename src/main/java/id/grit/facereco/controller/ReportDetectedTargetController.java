package id.grit.facereco.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.grit.facereco.model.request.ReportDetectedTargetReq;
import id.grit.facereco.services.ReportDetectedTargetServices;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/v2/report-detected-target")
public class ReportDetectedTargetController {
    
     private final ReportDetectedTargetServices reportDetectedTargetServices;

    public ReportDetectedTargetController(ReportDetectedTargetServices reportDetectedTargetServices) {
        this.reportDetectedTargetServices = reportDetectedTargetServices;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReportDetectedTargetReq request, HttpServletRequest httpRequest) {
        return reportDetectedTargetServices.createReportDetectedTarget(request, httpRequest);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<?> getById(@PathVariable UUID uuid) {
        return reportDetectedTargetServices.getReportDetectedTarget(uuid);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return reportDetectedTargetServices.getAllReportDetectedTargets();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> update(
            @PathVariable UUID uuid,
            @RequestBody ReportDetectedTargetReq request,
            HttpServletRequest httpRequest) {
        return reportDetectedTargetServices.updateReportDetectedTarget(uuid, request, httpRequest);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> delete(@PathVariable UUID uuid) {
        return reportDetectedTargetServices.deleteReportDetectedTarget(uuid);
    }

}
