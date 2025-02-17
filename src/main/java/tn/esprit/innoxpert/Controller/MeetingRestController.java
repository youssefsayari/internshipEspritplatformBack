package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Service.MeetingService;
import tn.esprit.innoxpert.Service.MeetingServiceInterface;

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




}
