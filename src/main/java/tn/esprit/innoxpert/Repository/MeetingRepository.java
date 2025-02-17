package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long> {
}
