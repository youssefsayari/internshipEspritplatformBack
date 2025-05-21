package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String message;

    Date dateEnvoi;

    boolean vue = false;

    @ManyToOne
    User user;

    @ManyToOne
    Quiz quiz;
}
