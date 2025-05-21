package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.AddInternship;
import tn.esprit.innoxpert.DTO.InternshipAdminResponse;
import tn.esprit.innoxpert.DTO.InternshipResponse;
import tn.esprit.innoxpert.DTO.InternshipTutorResponse;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.Task;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Service.InternshipService;

import java.util.List;

@Tag(name = "Internship Management")
@RestController
@AllArgsConstructor
@RequestMapping("/internship")
public class InternshipRestController {
    @Autowired
    InternshipService internshipService;
    @GetMapping("/getAllInternships")
    public List<Internship> getAllInternships()
    {
        return internshipService.getAllInternships();
    }
    @GetMapping("/getInternshipByCriteria")
    public List<InternshipResponse> getInternshipsByCriteria(
            @RequestParam(required = false) Long idUser,
            @RequestParam(required = false) Long idPost) {
        List<InternshipResponse> internships = internshipService.getInternshipsByCriteria(idUser, idPost);
        return internships;
    }

    @GetMapping("/getInternshipsForAdmin")
    public List<InternshipAdminResponse> getInternshipsForAdmin(
            @RequestParam(required = false) Long idPost) {
        List<InternshipAdminResponse> internshipAdminResponse = internshipService.getInternshipsForAdmin(idPost);
        return internshipAdminResponse;
    }

    @GetMapping("/getInternshipById")
    public Internship getInternshipById(@RequestParam Long internshipId )
    {
        return internshipService.getInternshipById(internshipId);
    }
    @PostMapping("/addInternship")
    public ResponseEntity<String> addInternship (@RequestBody AddInternship addInternship)
    {
        try {
            internshipService.addInternship(addInternship);
            return ResponseEntity.ok("Internship request submitted successfully!");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @DeleteMapping("/removeInternshipById/{internshipId}")
    public ResponseEntity<String> removeInternshipById(@PathVariable Long internshipId) {
        internshipService.removeInternshipById(internshipId);
        return ResponseEntity.ok("Internship request removed successfully!");
    }


    @PutMapping("/updateInternship")

    public Internship updateInternship(@RequestBody Internship internship)
    {
        return internshipService.updateInternship(internship);
    }

    @PostMapping("/affectationV/{internshipId}/{tutorId}")
    public ResponseEntity<?> affectationValidator(@PathVariable Long internshipId, @PathVariable Long tutorId) {
        internshipService.affectationValidator(internshipId, tutorId);
        return ResponseEntity.ok("Tutor affected successfully");
    }

    @PostMapping("/approveInternship/{internshipId}")
    public ResponseEntity<String> approveInternship(@PathVariable Long internshipId) {
        try {
            internshipService.approveInternship(internshipId);
            return ResponseEntity.ok("Internship accepted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/rejectInternship/{internshipId}")
    public ResponseEntity<String> rejectInternship(@PathVariable Long internshipId) {
        try {
            internshipService.rejectInternship(internshipId);
            return ResponseEntity.ok("Internship application denied!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/getInternshipsForTutor")
    public List<InternshipTutorResponse> getInternshipsForTutor(
            @RequestParam(required = false) Long idUser) {
        List<InternshipTutorResponse> internshipTutorResponse = internshipService.getInternshipsForTutor(idUser);
        return internshipTutorResponse;
    }



}
