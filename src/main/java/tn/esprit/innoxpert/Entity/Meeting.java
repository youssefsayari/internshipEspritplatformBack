package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

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

     LocalDate date;
     int heure ;

     String description;

    @Enumerated(EnumType.STRING)
     TypeMeeting typeMeeting;

    @OneToOne
    User organiser;

    @OneToOne
    User participant;


    boolean approved;



}
