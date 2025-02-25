package id.grit.facereco.controller;

import java.util.Map;
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

import id.grit.facereco.model.request.ReportRequest;
import id.grit.facereco.services.ReportService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v2/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createReport(@RequestBody ReportRequest request, HttpServletRequest httpRequest) {
        return reportService.createReport(request, httpRequest);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getReports() {
        return reportService.getReports();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> updateReport(@PathVariable UUID uuid, @RequestBody ReportRequest request, HttpServletRequest httpRequest) {
        return reportService.updateReport(uuid, request, httpRequest);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> deleteReport(@PathVariable UUID uuid) {
        return reportService.deleteReport(uuid);
    }
}
