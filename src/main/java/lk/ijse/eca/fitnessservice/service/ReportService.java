package lk.ijse.eca.fitnessservice.service;

import lk.ijse.eca.fitnessservice.entity.FitnessReport;
import org.springframework.core.io.Resource;

public interface ReportService {
    FitnessReport generateFitnessReport(Long memberId);
    Resource getFitnessReportPdf(Long reportId);
}
