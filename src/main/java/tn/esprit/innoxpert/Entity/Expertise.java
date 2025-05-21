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
public class Expertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idExpertise;

    @Enumerated(EnumType.STRING)
    TypeExpertise typeExpertise;


}
