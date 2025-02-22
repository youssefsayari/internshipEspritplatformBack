package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.TypeMeeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long> {
    boolean existsByTypeMeeting(TypeMeeting typeMeeting);
    boolean existsByTypeMeetingAndIdMeetingNot(TypeMeeting typeMeeting, Long idMeeting);

}
