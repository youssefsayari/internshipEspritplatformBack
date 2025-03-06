package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @NotBlank(message = "La question est obligatoire")
    @Size(min = 10, max = 500, message = "La question doit contenir entre 10 et 500 caractères")
    String question;

    @NotBlank(message = "L'option 1 est obligatoire")
    String option1;

    @NotBlank(message = "L'option 2 est obligatoire")
    String option2;

    @NotBlank(message = "L'option 3 est obligatoire")
    String option3;

    @NotBlank(message = "L'option 4 est obligatoire")
    String option4;

    @NotBlank(message = "La réponse correcte est obligatoire")
    @Pattern(regexp = "option1|option2|option3|option4", message = "La réponse correcte doit être 'option1', 'option2', 'option3' ou 'option4'")
    String reponse_correcte;

    @ManyToOne
    Quiz quiz;
}

