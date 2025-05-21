package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Consultant;
import tn.esprit.innoxpert.Entity.Consultation;
import tn.esprit.innoxpert.Entity.JobOffer;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface ConsultationRepository extends JpaRepository<Consultation,Long> {
    @Query("SELECT c FROM Consultation c " +
            "WHERE c.consultant.id = :consultantId " +
            "AND ((c.slot.startTime < :endTime) AND (c.slot.endTime > :startTime))")
    List<Consultation> findByConsultantAndTimeRange(Long consultantId, LocalDateTime startTime, LocalDateTime endTime);

    List<Consultation> findByClientId(Long clientId);
    void deleteByClientId(Long clientId);
    List<Consultation> findByConsultantId(Long consultantId);
    @Query("SELECT c FROM Consultation c JOIN FETCH c.slot s WHERE c.consultant = :consultant AND s.startTime BETWEEN :start AND :end")
    List<Consultation> findByConsultantAndSlotStartTimeBetween(
            @Param("consultant") Consultant consultant,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
    boolean existsByConsultantAndSlotStartTime(Consultant consultant, LocalDateTime startTime);

}