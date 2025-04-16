package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.innoxpert.Entity.HistoryLog;

public interface HistoryLogRepository extends JpaRepository<HistoryLog, Long> {
}
