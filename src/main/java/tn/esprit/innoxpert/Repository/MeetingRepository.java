package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.TypeMeeting;
import tn.esprit.innoxpert.Entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long> {
    boolean existsByTypeMeeting(TypeMeeting typeMeeting);
    boolean existsByTypeMeetingAndIdMeetingNot(TypeMeeting typeMeeting, Long idMeeting);

    List<Meeting> findByParticipant_IdUser(Long studentID);
    List<Meeting> findByOrganiser_IdUser(Long tutorID);
    List<Meeting>findByParticipant_IdUserAndOrganiser_IdUser(Long studentID,Long organiserId);


    boolean existsByTypeMeetingAndOrganiserAndParticipant(TypeMeeting typeMeeting, User organiser, User participant);

    boolean existsByTypeMeetingAndOrganiserAndParticipantAndIdMeetingNot(TypeMeeting typeMeeting, User organiser, User participant, Long meetingId);

    List<Meeting> findByDateAndHeureBetweenAndApprovedTrue(LocalDate date, String startTime, String endTime);

    List<Meeting> findByDateAndApprovedTrue(LocalDate date);

    List<Meeting> findByDateAndApprovedTrueAndNotifiedFalse(LocalDate date);

    @Query("SELECT m FROM Meeting m ORDER BY m.date desc ")
    List<Meeting> getAllMeetingsOrderedByDate();


    @Query("SELECT m.participant, COUNT(m) as meetingCount " +
            "FROM Meeting m " +
            "GROUP BY m.participant " +
            "ORDER BY meetingCount DESC")
    List<User> findMostActiveStudents();



}
