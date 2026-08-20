package lk.ijse.eca.fitnessservice.controller;

import lk.ijse.eca.fitnessservice.dto.ApiResponse;
import lk.ijse.eca.fitnessservice.entity.FitnessReport;
import lk.ijse.eca.fitnessservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fitness-reports")
@RequiredArgsConstructor
public class FitnessReportController {

    private final ReportService reportService;

    @PostMapping("/members/{memberId}/generate")
    public ResponseEntity<ApiResponse<FitnessReport>> generateFitnessReport(@PathVariable Long memberId) {
        FitnessReport report = reportService.generateFitnessReport(memberId);
        
        ApiResponse<FitnessReport> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setStatus(HttpStatus.CREATED.value());
        response.setPath("/api/v1/fitness-reports/members/" + memberId + "/generate");
        
        ApiResponse.DataWrapper<FitnessReport> data = new ApiResponse.DataWrapper<>();
        data.setMessage("Fitness report generated successfully");
        data.setContent(report);
        response.setData(data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{reportId}/pdf")
    public ResponseEntity<Resource> getFitnessReportPdf(@PathVariable Long reportId) {
        Resource resource = reportService.getFitnessReportPdf(reportId);
        
        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
