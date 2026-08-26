package lk.ijse.eca.fitnessservice.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lk.ijse.eca.fitnessservice.dto.FullProfileResponseDTO;
import lk.ijse.eca.fitnessservice.dto.MemberWorkoutResponseDTO;
import lk.ijse.eca.fitnessservice.entity.FitnessReport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PdfGeneratorService {

    public byte[] generateFitnessReportPdfBytes(FitnessReport report, FullProfileResponseDTO memberProfile, List<MemberWorkoutResponseDTO> workouts) {
        Document document = new Document();
        try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
            Paragraph title = new Paragraph("Fitness Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Report Date
            document.add(new Paragraph("Report Date: " + LocalDate.now()));
            document.add(new Paragraph(" "));

            // Member Info Table
            PdfPTable memberTable = new PdfPTable(2);
            memberTable.setWidthPercentage(100);
            memberTable.setSpacingBefore(10);
            memberTable.setSpacingAfter(20);

            addCell(memberTable, "Member Name", true);
            addCell(memberTable, memberProfile.getFirstName() + " " + memberProfile.getLastName(), false);
            addCell(memberTable, "Email", true);
            addCell(memberTable, memberProfile.getEmail(), false);
            addCell(memberTable, "Gender", true);
            addCell(memberTable, memberProfile.getGender() != null ? memberProfile.getGender() : "N/A", false);
            addCell(memberTable, "Assigned Trainer", true);
            if (memberProfile.getAssignedTrainer() != null) {
                addCell(memberTable, memberProfile.getAssignedTrainer().getFirstName() + " " + memberProfile.getAssignedTrainer().getLastName(), false);
            } else {
                addCell(memberTable, "None", false);
            }
            document.add(memberTable);

            // Workout Statistics
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Workout Summary", sectionFont));
            document.add(new Paragraph(" "));

            PdfPTable statsTable = new PdfPTable(2);
            statsTable.setWidthPercentage(100);
            statsTable.setSpacingAfter(20);
            addCell(statsTable, "Total Workouts", true);
            addCell(statsTable, String.valueOf(report.getTotalWorkouts()), false);
            addCell(statsTable, "Completed Workouts", true);
            addCell(statsTable, String.valueOf(report.getCompletedWorkouts()), false);
            addCell(statsTable, "Incomplete Workouts", true);
            addCell(statsTable, String.valueOf(report.getIncompleteWorkouts()), false);
            addCell(statsTable, "Completion Rate", true);
            addCell(statsTable, String.format("%.2f%%", report.getCompletionRate()), false);
            document.add(statsTable);

            // Current Workout Plan
            document.add(new Paragraph("Current Workout Plan", sectionFont));
            document.add(new Paragraph(" "));
            
            // Find most recent assigned or in-progress plan
            Optional<MemberWorkoutResponseDTO> activeWorkout = workouts.stream()
                    .filter(w -> "IN_PROGRESS".equals(w.getStatus()) || "ASSIGNED".equals(w.getStatus()))
                    .findFirst();

            if (activeWorkout.isPresent()) {
                document.add(new Paragraph("Plan Name: " + activeWorkout.get().getName()));
                document.add(new Paragraph("Description: " + activeWorkout.get().getDescription()));
                document.add(new Paragraph("Status: " + activeWorkout.get().getStatus()));
                document.add(new Paragraph("Assigned Date: " + activeWorkout.get().getAssignedDate()));
            } else {
                document.add(new Paragraph("No active workout plan found."));
            }

            document.close();
            return out.toByteArray();

        } catch (DocumentException | IOException e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    private void addCell(PdfPTable table, String text, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        if (isHeader) {
            cell.setBackgroundColor(new java.awt.Color(230, 230, 230)); // Light gray
        }
        cell.setPadding(8);
        table.addCell(cell);
    }
}
