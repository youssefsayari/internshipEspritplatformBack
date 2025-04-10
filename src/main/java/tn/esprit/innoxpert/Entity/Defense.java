package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    @JoinColumn(name = "student_id", unique = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUser")
    @JsonIdentityReference(alwaysAsId = false)
    User student;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "defense_tutors",
            joinColumns = @JoinColumn(name = "defense_id"),
            inverseJoinColumns = @JoinColumn(name = "tutor_id")
    )
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUser")
    @JsonIdentityReference(alwaysAsId = false)
    Set<User> tutors;


}