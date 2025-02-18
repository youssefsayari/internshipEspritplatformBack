package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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
    String titre;
    String description;
    Date date_passage;
    @ManyToOne(cascade = CascadeType.ALL)
    Societe societe;






}
