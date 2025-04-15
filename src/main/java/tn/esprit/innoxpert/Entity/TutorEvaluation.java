package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TutorEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "defense_id")
    @JsonBackReference  // Prevents circular reference with Defense entity
    Defense defense;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    @JsonBackReference  // Prevents circular reference with User entity
    User tutor;

    Double grade; // out of 20
    String remarks;

    @Enumerated(EnumType.STRING)
    EvaluationStatus status = EvaluationStatus.PENDING;
}
