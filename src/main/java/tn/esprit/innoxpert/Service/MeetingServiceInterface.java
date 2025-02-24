package tn.esprit.innoxpert.Service;

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
    public List<User> getStudentsByTutor(Long tutorId);



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
