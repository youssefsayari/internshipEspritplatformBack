package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.innoxpert.Entity.Consultant;
import tn.esprit.innoxpert.Entity.TimeSlot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    Optional<TimeSlot> findByConsultantAndStartTime(Consultant consultant, LocalDateTime startTime);

    List<TimeSlot> findByConsultantAndStartTimeBetween(
            Consultant consultant,
            LocalDateTime start,
            LocalDateTime end
    );
}
