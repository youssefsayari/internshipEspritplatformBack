package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.StudentTutor;
import tn.esprit.innoxpert.Entity.TypeMeeting;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.MeetingRepository;
import tn.esprit.innoxpert.Repository.StudentTutorRepository;
import tn.esprit.innoxpert.Repository.UserRepository;
import tn.esprit.innoxpert.Util.JitsiMeetingService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MeetingService implements MeetingServiceInterface {
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    StudentTutorRepository studentTutorRepository;
    @Autowired
    UserRepository userRepository;




   @Override
    public List<User> getStudentsByTutor(Long tutorId) {
        List<StudentTutor> relations = studentTutorRepository.findByTutorId(tutorId);
        List<Long> studentIds = relations.stream()
                .map(relation -> relation.getStudent().getIdUser())
                .collect(Collectors.toList());
        return userRepository.findAllById(studentIds);
    }

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
        if ((b.getTypeMeeting() == TypeMeeting.Restitution1 || b.getTypeMeeting() == TypeMeeting.Restitution2)
                && meetingRepository.existsByTypeMeeting(b.getTypeMeeting())) {
            throw new IllegalStateException("Cannot add more than one meeting of type " + b.getTypeMeeting());
        }

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
        if (!meetingRepository.existsById(b.getIdMeeting())) {
            throw new NotFoundException("Meeting with ID: " + b.getIdMeeting() + " was not found. Cannot update.");
        }

        Meeting existingMeeting = meetingRepository.findById(b.getIdMeeting()).orElseThrow(() ->
                new NotFoundException("Meeting with ID: " + b.getIdMeeting() + " not found"));

        if ((b.getTypeMeeting() == TypeMeeting.Restitution1 || b.getTypeMeeting() == TypeMeeting.Restitution2)
                && !existingMeeting.getTypeMeeting().equals(b.getTypeMeeting())
                && meetingRepository.existsByTypeMeetingAndIdMeetingNot(b.getTypeMeeting(), b.getIdMeeting())) {
            throw new IllegalStateException("Cannot have more than one meeting of type " + b.getTypeMeeting());
        }

        return meetingRepository.save(b);
    }

    @Override
    public Meeting updateMeetingAndAffectToParticipant(Meeting b, Long organiserId, Long participantId) {
        Meeting existingMeeting = meetingRepository.findById(b.getIdMeeting()).orElseThrow(() ->
                new NotFoundException("Meeting with ID: " + b.getIdMeeting() + " not found"));

        if ((b.getTypeMeeting() == TypeMeeting.Restitution1 || b.getTypeMeeting() == TypeMeeting.Restitution2)
                && !existingMeeting.getTypeMeeting().equals(b.getTypeMeeting())
                && meetingRepository.existsByTypeMeetingAndIdMeetingNot(b.getTypeMeeting(), b.getIdMeeting())) {
            throw new IllegalStateException("Cannot have more than one meeting of type " + b.getTypeMeeting());
        }

        User organiser = userRepository.findById(organiserId)
                .orElseThrow(() -> new NotFoundException("Organiser with ID: " + organiserId + " not found"));

        User participant = userRepository.findById(participantId)
                .orElseThrow(() -> new NotFoundException("Participant with ID: " + participantId + " not found"));

        existingMeeting.setDate(b.getDate());
        existingMeeting.setHeure(b.getHeure());
        existingMeeting.setDescription(b.getDescription());
        existingMeeting.setTypeMeeting(b.getTypeMeeting());
        existingMeeting.setOrganiser(organiser);
        existingMeeting.setParticipant(participant);

        return meetingRepository.save(existingMeeting);
    }

    @Override
    public List<Meeting> findByParticipant(Long studentId) {
        return meetingRepository.findByParticipant_IdUser(studentId);
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
    public String generateMeeting(String title) {

        return new JitsiMeetingService().generateMeetingLink(title);
    }

    @Override
    public Meeting approveMeetingById(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException("Meeting with ID: " + meetingId + " was not found. Cannot update."));

        meeting.setApproved(true);
        return meetingRepository.save(meeting);
    }


    public Meeting addMeetingAndAffectToParticipant(Meeting meeting, Long organiserId, Long participantId) {
        if ((meeting.getTypeMeeting() == TypeMeeting.Restitution1 || meeting.getTypeMeeting() == TypeMeeting.Restitution2)
                && meetingRepository.existsByTypeMeeting(meeting.getTypeMeeting())) {
            throw new IllegalStateException("Cannot add more than one meeting of type " + meeting.getTypeMeeting());
        }

        User organiser = userRepository.findById(organiserId).get();
        User participant = userRepository.findById(participantId).get();

        meeting.setOrganiser(organiser);
        meeting.setParticipant(participant);

        return meetingRepository.save(meeting);
    }



}
