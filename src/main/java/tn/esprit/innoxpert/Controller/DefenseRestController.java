package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.DefenseRequest;
import tn.esprit.innoxpert.DTO.DefenseWithEvaluationsDTO;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.TutorEvaluation;
import tn.esprit.innoxpert.Exceptions.SchedulingConflictException;
import tn.esprit.innoxpert.Service.DefenseServiceInterface;
import tn.esprit.innoxpert.Service.EvaluationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Tag(name = "Defense Management")
@RestController
@AllArgsConstructor
@RequestMapping("/defense")
public class DefenseRestController {

    private final DefenseServiceInterface defenseService;
    private final EvaluationService evaluationService;

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
            Defense defense = defenseService.addDefense(studentId, defenseRequest);
            return ResponseEntity.ok(defense);
        } catch (SchedulingConflictException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Scheduling conflict", "message", e.getMessage())
            );
        }
    }

    @DeleteMapping("/deleteDefense/{idDefense}")
    public void deleteDefenseById(@PathVariable("idDefense") Long idDefense) {
        defenseService.removeDefenseById(idDefense);
    }

    @PutMapping("/updateDefense")
    public ResponseEntity<?> updateDefense(@RequestBody Defense defense) {
        try {
            Defense updatedDefense = defenseService.updateDefense(defense);
            return ResponseEntity.ok(updatedDefense);
        } catch (SchedulingConflictException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Scheduling conflict", "message", e.getMessage())
            );
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


    // Or for static tutor ID = 2 (if you really want it hardcoded)
    @GetMapping("/getDefensesForTutor2")
    public List<Defense> getDefensesForStaticTutor() {
        return defenseService.getDefensesByTutorId(2L);
    }


}