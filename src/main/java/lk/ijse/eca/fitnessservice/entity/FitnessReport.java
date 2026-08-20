package lk.ijse.eca.fitnessservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "fitness_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FitnessReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long trainerId;

    private Integer totalWorkouts;
    private Integer completedWorkouts;
    private Integer incompleteWorkouts;
    private Double completionRate;

    @Column(nullable = false)
    private LocalDate reportDate;

    @Column(nullable = false)
    private String pdfFilePath;
}
