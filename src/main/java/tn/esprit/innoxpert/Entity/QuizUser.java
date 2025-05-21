
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


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class QuizUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuizUser;
    private Long idUser;
    private Long idQuiz;
    private int score;

}
