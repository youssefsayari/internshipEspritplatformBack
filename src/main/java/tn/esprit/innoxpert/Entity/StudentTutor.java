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
public class StudentTutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idUser;

    @OneToOne
    User student ;
    @OneToOne
    User tutor ;


}
