package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Defense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idDefense;

    LocalDate defenseDate;
    LocalTime defenseTime;
    String classroom;
    boolean reportSubmitted;
    boolean internshipCompleted;
    double defenseDegree;

    @OneToOne
    @JoinColumn(name = "student_id", unique = true) // Ensures one student has only one defense
    User student;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "defense_tutors",
            joinColumns = @JoinColumn(name = "defense_id"),
            inverseJoinColumns = @JoinColumn(name = "tutor_id")
    )
    Set<User> tutors;

}
