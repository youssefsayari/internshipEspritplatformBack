package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Getter
@Setter
public class DefenseWithEvaluationsDTO {
    private Long idDefense;
    private String classroom;
    private LocalDate defenseDate;
    private LocalTime defenseTime;
    private boolean reportSubmitted;
    private boolean internshipCompleted;
    private double defenseDegree;
    private UserDTO student;
    private List<Long> tutors;
    private List<TutorEvaluationDTO> evaluations;

    // Getters and setters
}

