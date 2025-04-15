package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Defense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idDefense;

    @NotNull(message = "Defense date cannot be null")
    LocalDate defenseDate;

    @NotNull(message = "Defense time cannot be null")

    LocalTime defenseTime;

    @Size(min = 1, message = "Classroom name should not be empty")
    String classroom;

    boolean reportSubmitted;
    boolean internshipCompleted;

    Double defenseDegree;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUser")
    @JsonIdentityReference(alwaysAsId = false)
    @NotNull(message = "Student cannot be null")
    User student;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "defense_tutors",
            joinColumns = @JoinColumn(name = "defense_id"),
            inverseJoinColumns = @JoinColumn(name = "tutor_id")
    )
    @JsonManagedReference
    @NotNull(message = "At least 3 tutors must be assigned to the defense")
    @Size(min = 3, max = 3, message = "A defense must have exactly 3 tutors")// Manages tutors in the defense entity
    Set<User> tutors;

    @OneToMany(mappedBy = "defense", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference  // Prevents circular reference with TutorEvaluation
    Set<TutorEvaluation> evaluations = new HashSet<>();

    public boolean areAllEvaluationsSubmitted() {
        return evaluations != null && evaluations.size() == 3 &&
                evaluations.stream().allMatch(evaluation -> evaluation.getStatus() == EvaluationStatus.SUBMITTED);
    }

    // Check if the defense date is today or in the future
    private boolean isDefenseDateValid() {
        return !defenseDate.isBefore(LocalDate.now());
    }

    // Check if the defense time is within the allowed time range (08:00 to 18:00)
    private boolean isDefenseTimeValid() {
        LocalTime minTime = LocalTime.parse("08:00", DateTimeFormatter.ISO_LOCAL_TIME);
        LocalTime maxTime = LocalTime.parse("18:00", DateTimeFormatter.ISO_LOCAL_TIME);
        return !defenseTime.isBefore(minTime) && !defenseTime.isAfter(maxTime);
    }
    // Validation method to check if all rules are met
    public boolean isValidDefense() {
        return isDefenseDateValid() && isDefenseTimeValid() && tutors.size() == 3;
    }

}
