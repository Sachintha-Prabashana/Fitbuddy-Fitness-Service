package lk.ijse.eca.fitnessservice.repository;

import lk.ijse.eca.fitnessservice.entity.FitnessReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FitnessReportRepository extends JpaRepository<FitnessReport, Long> {
    List<FitnessReport> findByMemberId(Long memberId);
}
