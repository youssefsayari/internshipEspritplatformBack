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
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idTask;

    String Description ;
    @NotBlank(message = "La description ne peut pas être vide")
    @Size(min = 5, max = 255, message = "La description doit contenir entre 5 et 255 caractères")
    String description;

    @NotNull(message = "Le statut de la tâche est obligatoire")
    @Enumerated(EnumType.STRING)
    TypeStatus status ;

    @ManyToOne
    @JsonIgnore
    @NotNull(message = "Un étudiant doit être assigné à cette tâche")
    User student;
}
