package tn.esprit.innoxpert.DTO;

import lombok.Data;
import tn.esprit.innoxpert.Entity.EvaluationStatus;
@Data
public class EvaluationResponse {
    private Long id;
    private Long defenseId;
    private Long tutorId;
    private Double grade;
    private String remarks;
    private EvaluationStatus status;
}