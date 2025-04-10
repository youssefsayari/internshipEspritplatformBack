package tn.esprit.innoxpert.Entity;

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
    Defense defense;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    User tutor;

    Double grade; // out of 20
    String remarks;

    @Enumerated(EnumType.STRING)
    EvaluationStatus status = EvaluationStatus.PENDING;
}

