package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Meeting;

import java.util.List;

public interface MeetingServiceInterface {
    public List<Meeting> getAllMeetings();
    public Meeting getMeetingById(Long meetingId);
    public Meeting addMeeting(Meeting b);
    public void removeMeetingById(Long meetingId);
    public Meeting updateMeeting (Meeting b );
    public Meeting approveMeeting(Meeting b);
    public Meeting approveMeetingById(Long meetingId) ;


}
