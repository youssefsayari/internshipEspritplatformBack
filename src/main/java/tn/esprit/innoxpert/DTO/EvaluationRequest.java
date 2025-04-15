package tn.esprit.innoxpert.DTO;

import lombok.Data;

@Data
public class EvaluationRequest {
    private Long defenseId;
    private Long tutorId;
    private Double grade;
    private String remarks;
}