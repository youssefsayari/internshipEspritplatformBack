package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.TimeLineRequest;
import tn.esprit.innoxpert.DTO.TimeLineResponse;
import tn.esprit.innoxpert.Entity.TimeLine;
import tn.esprit.innoxpert.Service.AgreementService;
import tn.esprit.innoxpert.Service.TimeLineService;

import java.util.List;

@Tag(name = "TimeLine Management")
@RestController
@AllArgsConstructor
@RequestMapping("/timeline")
public class TimeLineRestController {
    @Autowired
    TimeLineService timeLineService;

    @PostMapping("/add")
    public ResponseEntity<Void> addTimeLine(@RequestBody TimeLineRequest timeLineRequest) {
        timeLineService.addTimeLine(timeLineRequest.getUserId(), timeLineRequest.getAgreementId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public List<TimeLineResponse> getTimeLinesByUserId(@PathVariable Long userId) {
        return timeLineService.getTimeLinesByUserId(userId);
    }

    @PutMapping("/accept-step")
    public ResponseEntity<String> acceptStep(@RequestParam String title, @RequestParam Long userId) {
        timeLineService.acceptStep(title, userId);
        return ResponseEntity.ok("Step accepted successfully.");
    }

    @PutMapping("/reject-step")
    public ResponseEntity<String> rejectStep(@RequestParam String title, @RequestParam Long userId) {
        timeLineService.rejectStep(title, userId);
        return ResponseEntity.ok("Step rejected successfully.");
    }



}
