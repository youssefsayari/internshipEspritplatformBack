package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actionType;    // Exemple: "Création", "Réponse admin", etc.

    @Column(length = 1000)
    private String description;   // Détail de l'action

    private LocalDateTime timestamp;
}
