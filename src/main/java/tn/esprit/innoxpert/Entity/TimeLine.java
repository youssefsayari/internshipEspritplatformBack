package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TimeLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotNull(message = "title est obligatoire.")
    String title;

    @NotNull(message = "description est obligatoire.")
    String description;

    @NotNull(message = "dateLimite est obligatoire.")
    Date dateLimite;

    @ManyToOne
    @JoinColumn(name = "student_id_user")
    @JsonIgnore
    private User student;

    @ManyToOne
    @JoinColumn(name = "document_id")
    @JsonIgnore
    private Document document;

}
