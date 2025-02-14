package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.MeetingRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class MeetingService implements MeetingServiceInterface {
    @Autowired
    MeetingRepository meetingRepository;

    @Override
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    @Override
    public Meeting getMeetingById(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException("Meeting with ID : " + meetingId + " was not found."));
    }


    @Override
    public Meeting addMeeting(Meeting b) {
        b.setApproved(false);
        return meetingRepository.save(b);
    }

    @Override
    public void removeMeetingById(Long meetingId) {
        if (!meetingRepository.existsById(meetingId)) {
            throw new NotFoundException("Meeting with ID :  " + meetingId + " was not found.");
        }
        meetingRepository.deleteById(meetingId);
    }

    @Override
    public Meeting updateMeeting(Meeting b) {
        if ( !meetingRepository.existsById(b.getIdMeeting())) {
            throw new NotFoundException("Meeting with ID: " + b.getIdMeeting() + " was not found. Cannot update.");
        }
        return meetingRepository.save(b);
    }

    @Override
    public Meeting approveMeeting(Meeting b) {
        if ( !meetingRepository.existsById(b.getIdMeeting())) {
            throw new NotFoundException("Meeting with ID: " + b.getIdMeeting() + " was not found. Cannot update.");
        }
        b.setApproved(true);
        return meetingRepository.save(b);
    }

    @Override
    public Meeting approveMeetingById(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException("Meeting with ID: " + meetingId + " was not found. Cannot update."));

        meeting.setApproved(true);
        return meetingRepository.save(meeting);
    }

}
