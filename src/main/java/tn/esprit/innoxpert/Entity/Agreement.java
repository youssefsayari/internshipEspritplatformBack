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
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Date dateDebut;
    Date dateFin;

    @ManyToOne
    @JoinColumn(name = "representative_id_user")
    private User representative;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_id_user")
    private User user;

}
