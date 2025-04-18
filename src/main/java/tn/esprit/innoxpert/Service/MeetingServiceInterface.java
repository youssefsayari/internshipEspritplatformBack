package tn.esprit.innoxpert.Service;

import org.springframework.data.repository.query.Param;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

public interface MeetingServiceInterface {
    public List<Meeting> getAllMeetings();
    public Meeting getMeetingById(Long meetingId);
    public Meeting addMeeting(Meeting b);
    public void removeMeetingById(Long meetingId);
    public Meeting updateMeeting (Meeting b );
    public Meeting approveMeeting(Meeting b);
    public Meeting approveMeetingById(Long meetingId) ;
    public List<User> getStudentsByTutor(User tutor);
    public Meeting addMeetingAndAffectToParticipant(Meeting meeting, Long organiserId, Long participantId) ;
    public Meeting updateMeetingAndAffectToParticipant(Meeting b, Long organiserId, Long participantId) ;
    public List<Meeting> findByParticipant(Long studentId);
    public List<Meeting> findByOrganiser(Long tutorId);
    public List<Meeting> findByParticipantAndOrganiser(Long studentId,Long organiserId);
    Long findTutorIdByStudentId(Long studentId);




    public Meeting disapproveMeetingById(Long meetingId, String reason) ;

    public void notifyUsersOneDayBeforeMeeting();

    public List<User> getMostActiveStudents() ;







        public String generateMeeting(String title);
    /*
    public void sendEmailWithLink(Long idReceiver, String link);
    public void notifyUpcomingMeetings();
    public String GenerateQrCodeForMeetingLink();
    public void autoApproveMeetingsfornext24();
    public int countMeetingsByUser(Long userId);
    public List<Meeting> listMeetingsForNextWeek();
    public List<Integer> suggestAlternativeTimes(Long meetingId);





     */





}
