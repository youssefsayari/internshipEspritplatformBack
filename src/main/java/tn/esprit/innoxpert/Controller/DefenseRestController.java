package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.DefenseRequest;
import tn.esprit.innoxpert.DTO.DefenseWithEvaluationsDTO;
import tn.esprit.innoxpert.DTO.UserRole;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.TutorEvaluation;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.SchedulingConflictException;
import tn.esprit.innoxpert.Service.DefenseServiceInterface;
import tn.esprit.innoxpert.Service.EvaluationService;
import tn.esprit.innoxpert.Service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Tag(name = "Defense Management")
@RestController
@AllArgsConstructor
@RequestMapping("/defense")
public class DefenseRestController {

    private final DefenseServiceInterface defenseService;
    private final EvaluationService evaluationService;
    private final UserService userService;

    @GetMapping("/getAllDefenses")
    public List<Defense> getAllDefenses() {
        return defenseService.getAllDefenses();
    }

    @GetMapping("/getDefenseById/{defenseId}")
    public ResponseEntity<Defense> getDefenseById(@PathVariable Long defenseId) {
        Defense defense = defenseService.getDfenseById(defenseId); // Ensure this returns a valid Defense object
        return ResponseEntity.ok(defense);
    }


    @PostMapping("/defense/{studentId}/defenses")
    public ResponseEntity<?> createDefense(
            @PathVariable Long studentId,
            @RequestBody DefenseRequest defenseRequest) {
        try {
            // Call the service to add the defense
            Defense defense = defenseService.addDefense(studentId, defenseRequest);
            return ResponseEntity.ok(defense);
        } catch (IllegalArgumentException e) {
            // Handle IllegalArgumentException (e.g., student already has a defense)
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (SchedulingConflictException e) {
            // Handle scheduling conflict errors
            return ResponseEntity.badRequest().body(Map.of("error", "Scheduling conflict", "message", e.getMessage()));
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred"));
        }
    }


    @DeleteMapping("/deleteDefense/{idDefense}")
    public void deleteDefenseById(@PathVariable("idDefense") Long idDefense) {
        defenseService.removeDefenseById(idDefense);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateDefense(
            @PathVariable("id") Long defenseId,
            @RequestBody DefenseRequest defenseRequest
    ) {
        try {
            Defense updatedDefense = defenseService.updateDefense(defenseId, defenseRequest);
            return ResponseEntity.ok(updatedDefense);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (SchedulingConflictException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Scheduling conflict", "message", e.getMessage()));
        }
    }


    @GetMapping("/check-availability")
    public ResponseEntity<?> checkAvailability(
            @RequestParam String classroom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time) {

        boolean isAvailable = defenseService.isDefenseSlotAvailable(classroom, date, time);
        return ResponseEntity.ok(Map.of("available", isAvailable));
    }

    @GetMapping("/getDefensesByTutor/{tutorId}")
    public ResponseEntity<List<DefenseWithEvaluationsDTO>> getDefensesWithEvaluationsByTutor(
            @PathVariable("tutorId") Long tutorId) {
        List<DefenseWithEvaluationsDTO> defenses = defenseService.getDefensesWithEvaluationsByTutor(tutorId);
        return ResponseEntity.ok(defenses);
    }

    @GetMapping("/getDefensesByStudent/{studentId}")
    public ResponseEntity<List<Defense>> getDefensesByStudentId(@PathVariable("studentId") Long studentId) {
        List<Defense> defenses = defenseService.getDefensesByStudentId(studentId);
        return ResponseEntity.ok(defenses);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, List<Defense>>> getDefenseStats() {
        List<Defense> allDefenses = defenseService.getAllDefenses(); // Fetch all defenses from the service
        Map<String, List<Defense>> stats = defenseService.getDefenseStats(allDefenses);
        return ResponseEntity.ok(stats); // Return the categorized stats
    }


    @GetMapping("/getDefensesForTutor2")
    public List<Defense> getDefensesForStaticTutor() {
        return defenseService.getDefensesByTutorId(2L);
    }

    @GetMapping("/generate-evaluation-grid/{defenseId}")
    public ResponseEntity<byte[]> generateEvaluationGrid(
            @PathVariable Long defenseId) throws IOException {

        // Generate the evaluation grid as a byte array
        byte[] pdfBytes = defenseService.generateEvaluationGrid(defenseId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/download-evaluation-grid/{defenseId}")
    public ResponseEntity<byte[]> downloadEvaluationGrid(
            @PathVariable Long defenseId) throws IOException {

        // Generate the evaluation grid as a byte array
        byte[] pdfBytes = defenseService.generateEvaluationGrid(defenseId);

        // Fetch defense and student data to create a proper file name
        Defense defense = defenseService.getDfenseById(defenseId); // Ensure this returns a valid Defense object
        String studentName = defense.getStudent().getLastName() + "_" + defense.getStudent().getFirstName();
        String fileName = "EvaluationGrid_" + studentName + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}