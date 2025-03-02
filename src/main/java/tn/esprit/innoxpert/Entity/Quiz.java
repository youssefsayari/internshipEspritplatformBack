package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idQuiz;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 5, max = 100, message = "Le titre doit contenir entre 5 et 100 caractères")
    String titre;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, max = 500, message = "La description doit contenir entre 10 et 500 caractères")
    String description;

    @Future(message = "La date de passage doit être dans le futur")
    @NotNull(message = "La date de passage est obligatoire")
    Date date_passage;

    @ManyToOne
    @JsonIgnore

    Societe societe;
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)

    private List<QuestionReponse> questions = new ArrayList<>();
}
