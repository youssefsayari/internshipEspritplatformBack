package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.EvaluationRequest;
import tn.esprit.innoxpert.DTO.EvaluationResponse;
import tn.esprit.innoxpert.Service.EvaluationService;

import java.util.List;

@Tag(name = "Defense Evaluation Management")
@RestController
@AllArgsConstructor
@RequestMapping("/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<EvaluationResponse> submitEvaluation(@RequestBody EvaluationRequest request) {
        return ResponseEntity.ok(evaluationService.submitEvaluation(request));
    }

    @GetMapping("/defense/{defenseId}")
    public ResponseEntity<List<EvaluationResponse>> getDefenseEvaluations(@PathVariable Long defenseId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsForDefense(defenseId));
    }

    @GetMapping("/{evaluationId}")
    public ResponseEntity<EvaluationResponse> getEvaluation(@PathVariable Long evaluationId) {
        return ResponseEntity.ok(evaluationService.getEvaluation(evaluationId));
    }
}