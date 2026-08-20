package lk.ijse.eca.fitnessservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberWorkoutResponseDTO {
    private String id;
    private Long memberId;
    private Long trainerId;
    private String planId;
    private String name;
    private String description;
    private String status; // ASSIGNED, IN_PROGRESS, COMPLETED, SKIPPED
    private LocalDate assignedDate;
}
