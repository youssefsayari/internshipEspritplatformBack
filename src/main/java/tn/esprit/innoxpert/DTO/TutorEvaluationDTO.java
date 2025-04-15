package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TutorEvaluationDTO {
    private Long id;
    private Long tutorId;
    private double grade;
    private String remarks;
    private String status;
}
