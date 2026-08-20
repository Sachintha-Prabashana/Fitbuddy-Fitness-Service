package lk.ijse.eca.fitnessservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FullProfileResponseDTO {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String profileImageUrl;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private TrainerSummaryDTO assignedTrainer;

    @Data
    public static class TrainerSummaryDTO {
        private Long id;
        private Long userId;
        private String firstName;
        private String lastName;
        private String specialization;
        private String phone;
    }
}
