package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
public class DefenseRequest {
    private LocalDate defenseDate;
    private LocalTime defenseTime;
    private String classroom;
    private boolean reportSubmitted;
    private boolean internshipCompleted;
    private double defenseDegree;
    private Set<Long> tutorIds;
}
