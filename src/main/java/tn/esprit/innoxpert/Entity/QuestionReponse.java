package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class QuestionReponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idQuestionReponse;
    String question;
    String option1;
    String option2;
    String option3;
    String option4;
    String reponse_correcte;
    @ManyToOne(cascade = CascadeType.ALL)
    Quiz quiz;







}
