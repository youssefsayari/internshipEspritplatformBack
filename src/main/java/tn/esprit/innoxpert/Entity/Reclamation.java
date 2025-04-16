package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Le sujet ne doit pas être vide.")
    @Pattern(regexp = ".*[A-Za-z].*", message = "Le sujet ne peut pas être uniquement numérique.")
    @Size(min = 3, max = 100, message = "Le sujet doit faire entre 3 et 100 caractères.")
    String subject;

    @Column(length = 1000)
    private String response;

    @NotBlank(message = "La description ne doit pas être vide.")
    @Size(min = 10, max = 1000, message = "La description doit faire entre 10 et 1000 caractères.")
    @Column(length = 1000)
    String description;

    LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    ReclamationStatus status = ReclamationStatus.PENDING;

    private boolean rejectedByUser = false;

    @ManyToOne
    private User createdBy;

    @Column(length = 1000)
    String adminResponse;

    Boolean adminResponded = false;

    @ManyToOne
    private User adminPriseEnCharge;

    // Ajouts nécessaires pour le front
    @JsonProperty("userId")
    public Long getUserId() {
        return createdBy != null ? createdBy.getIdUser() : null;
    }

    @JsonProperty("adminId")
    public Long getAdminId() {
        return adminPriseEnCharge != null ? adminPriseEnCharge.getIdUser() : null;
    }
}
