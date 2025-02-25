package tn.esprit.innoxpert.Entity;

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
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idMeeting;

    @NotNull(message = "La date ne peut pas être nulle")
    @FutureOrPresent(message = "La date doit être dans le présent ou le futur")
    LocalDate date;

    @NotBlank(message = "L'heure ne peut pas être vide")
    @Pattern(
            regexp = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$",
            message = "L'heure doit être au format HH:mm (ex: 14:30)"
    )
    String heure;

    @NotBlank(message = "La description ne peut pas être vide")
    @Size(min = 5, max = 255, message = "La description doit contenir entre 5 et 255 caractères")
    String description;

    @NotNull(message = "Le type de meeting est obligatoire")
    @Enumerated(EnumType.STRING)
    TypeMeeting typeMeeting;

    @ManyToOne
    @JoinColumn(name = "organiser_id")
    @NotNull(message = "L'organisateur est obligatoire")
    User organiser;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    @NotNull(message = "Le participant est obligatoire")
    User participant;
    String link;
    boolean approved;
}
