package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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

    @NotBlank(message = "La description ne peut pas être vide")
    @Size(min = 5, max = 255, message = "La description doit contenir entre 5 et 255 caractères")
    String description;

    @NotNull(message = "Le statut de la tâche est obligatoire")
    @Enumerated(EnumType.STRING)
    TypeStatus status;

    @ManyToOne
    @JsonIgnore
    @NotNull(message = "Un étudiant doit être assigné à cette tâche")
    User student;
    @Column
    Integer mark;

    @FutureOrPresent(message = "La date doit être dans le présent ou le futur")
    LocalDate deadline;

    boolean notified = false;
}