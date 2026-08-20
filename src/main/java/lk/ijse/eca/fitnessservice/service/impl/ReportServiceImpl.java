package lk.ijse.eca.fitnessservice.service.impl;

import lk.ijse.eca.fitnessservice.dto.FullProfileResponseDTO;
import lk.ijse.eca.fitnessservice.dto.MemberWorkoutResponseDTO;
import lk.ijse.eca.fitnessservice.entity.FitnessReport;
import lk.ijse.eca.fitnessservice.exception.ResourceNotFoundException;
import lk.ijse.eca.fitnessservice.repository.FitnessReportRepository;
import lk.ijse.eca.fitnessservice.service.MemberServiceClient;
import lk.ijse.eca.fitnessservice.service.PdfGeneratorService;
import lk.ijse.eca.fitnessservice.service.ReportService;
import lk.ijse.eca.fitnessservice.service.WorkoutServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final MemberServiceClient memberServiceClient;
    private final WorkoutServiceClient workoutServiceClient;
    private final PdfGeneratorService pdfGeneratorService;
    private final FitnessReportRepository fitnessReportRepository;

    public FitnessReport generateFitnessReport(Long memberId) {
        // 1. Fetch member details
        FullProfileResponseDTO memberProfile = memberServiceClient.getMemberProfile(memberId);

        // 2. Fetch all workouts
        List<MemberWorkoutResponseDTO> workouts = workoutServiceClient.getAllMemberWorkouts(memberId);

        // 3. Calculate statistics
        int totalWorkouts = workouts.size();
        int completedWorkouts = (int) workouts.stream()
                .filter(w -> "COMPLETED".equalsIgnoreCase(w.getStatus()))
                .count();
        
        int incompleteWorkouts = totalWorkouts - completedWorkouts;
        
        double completionRate = 0.0;
        if (totalWorkouts > 0) {
            completionRate = ((double) completedWorkouts / totalWorkouts) * 100;
        }

        // Determine trainer id
        Long trainerId = memberProfile.getAssignedTrainer() != null ? memberProfile.getAssignedTrainer().getId() : 0L;

        // 4. Create FitnessReport entity
        FitnessReport report = FitnessReport.builder()
                .memberId(memberId)
                .trainerId(trainerId)
                .totalWorkouts(totalWorkouts)
                .completedWorkouts(completedWorkouts)
                .incompleteWorkouts(incompleteWorkouts)
                .completionRate(completionRate)
                .reportDate(LocalDate.now())
                .pdfFilePath("") // Will update after generation
                .build();

        // 5. Generate PDF
        String pdfPath = pdfGeneratorService.generateFitnessReportPdf(report, memberProfile, workouts);
        
        // 6. Save and return
        report.setPdfFilePath(pdfPath);
        return fitnessReportRepository.save(report);
    }

    @Override
    public Resource getFitnessReportPdf(Long reportId) {
        FitnessReport report = fitnessReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Fitness report not found with id: " + reportId));
        
        try {
            java.nio.file.Path file = java.nio.file.Paths.get(report.getPdfFilePath());
            Resource resource = new org.springframework.core.io.UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (java.net.MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
