package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Service.MeetingService;
import tn.esprit.innoxpert.Service.MeetingServiceInterface;
import tn.esprit.innoxpert.Service.UserService;

import java.util.List;

@Tag(name = "Meeting Management")
@RestController
@AllArgsConstructor
@RequestMapping("/meeting")
public class MeetingRestController {
    @Autowired
    MeetingServiceInterface meetingService;

    @GetMapping("/getAllMeetings")
    public List<Meeting> getAllMeetings()
    {
        return meetingService.getAllMeetings();
    }
    @GetMapping("/getMeetingById/{idMeeting}")
    public Meeting getMeetingById(@PathVariable ("idMeeting") Long idMeeting)
    {
        return meetingService.getMeetingById(idMeeting);
    }
    @PostMapping("/addMeeting")
    public Meeting addMeeting ( @RequestBody Meeting meeting)
    {
        return meetingService.addMeeting(meeting);
    }

    @PostMapping("/addMeetingAndAffectToParticipant/{organiserId}/{participantId}")
    public ResponseEntity<Meeting> addMeetingAndAffectToParticipant(@RequestBody Meeting meeting,
                                                                    @PathVariable("organiserId") Long organiserId,
                                                                    @PathVariable("participantId") Long participantId) {
        try {
            Meeting savedMeeting = meetingService.addMeetingAndAffectToParticipant(meeting, organiserId, participantId);
            return ResponseEntity.ok(savedMeeting);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteMeeting/{idMeeting}")
    public void deleteMeetingById(@PathVariable ("idMeeting") Long idMeeting)
    {
        meetingService.removeMeetingById(idMeeting);
    }

    @PutMapping("/updateMeeting")

    public Meeting updateMeeting(@RequestBody Meeting meeting)
    {
        return meetingService.updateMeeting(meeting);
    }

    @PutMapping("/approveMeetingById/{idMeeting}")

    public Meeting approveMeeting(@PathVariable("idMeeting")Long idMeeting)
    {
        return meetingService.approveMeetingById(idMeeting);
    }

    @PutMapping("/disapproveMeetingById/{idMeeting}")

    public Meeting disapproveMeeting(@PathVariable("idMeeting")Long idMeeting,@RequestBody String reason)
    {
        return meetingService.disapproveMeetingById(idMeeting,reason);
    }



        @GetMapping("/getStudentsByTutorId/{idTutor}")
        public ResponseEntity<List<User>> getStudentsByTutorId(@PathVariable("idTutor") Long idTutor) {
            List<User> students = meetingService.getStudentsByTutor(idTutor);
            if (students.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(students);
        }

    @PutMapping("/updateMeetingAndAffectToParticipant/{organiserId}/{participantId}")
    public ResponseEntity<Meeting> updateMeetingAndAffectToParticipant(
            @RequestBody Meeting meetingDetails,
            @PathVariable Long organiserId,
            @PathVariable Long participantId) {

        Meeting updatedMeeting = meetingService.updateMeetingAndAffectToParticipant(meetingDetails, organiserId, participantId);
        return ResponseEntity.ok(updatedMeeting);
    }

    @GetMapping("/getMeetingsByStudent/{idStudent}")
    public List<Meeting> getMeetingsByStudent(@PathVariable("idStudent")Long idStudent)
    {
        return meetingService.findByParticipant(idStudent);
    }
    @GetMapping("/getMeetingsByTutor/{idTutor}")
    public List<Meeting>getMeetingsByTutor(@PathVariable("idTutor") Long idTutor)
    {
        return meetingService.findByOrganiser(idTutor);
    }


    @GetMapping("/getMeetingsByStudentAndTutor/{idStudent}/{idTutor}")
    public List<Meeting> getMeetingsByStudentAndTutor(@PathVariable("idStudent")Long idStudent,@PathVariable("idTutor")Long idTutor)
    {
        return meetingService.findByParticipantAndOrganiser(idStudent,idTutor);
    }











}





