package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.TypeMeeting;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long> {
    boolean existsByTypeMeeting(TypeMeeting typeMeeting);
    boolean existsByTypeMeetingAndIdMeetingNot(TypeMeeting typeMeeting, Long idMeeting);

    List<Meeting> findByParticipant_IdUser(Long studentID);


    boolean existsByTypeMeetingAndOrganiserAndParticipant(TypeMeeting typeMeeting, User organiser, User participant);

    boolean existsByTypeMeetingAndOrganiserAndParticipantAndIdMeetingNot(TypeMeeting typeMeeting, User organiser, User participant, Long meetingId);

}
