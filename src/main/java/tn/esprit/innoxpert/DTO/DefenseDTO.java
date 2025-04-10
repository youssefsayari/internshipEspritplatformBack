package tn.esprit.innoxpert.DTO;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefenseDTO {
    private Long studentId;
    private LocalDate defenseDate;
    private LocalTime defenseTime;
    private String classroom;
    private boolean reportSubmitted;
    private boolean internshipCompleted;
    private Set<Long> tutorIds;
}