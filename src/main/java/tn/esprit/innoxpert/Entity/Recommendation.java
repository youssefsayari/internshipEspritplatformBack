package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "TEXT")
    String content;

    LocalDate date;

    @ManyToOne
    @JoinColumn(name = "author_id")
    User author; // Tutor or Company

    @ManyToOne
    @JoinColumn(name = "recommended_id")
    User recommended; // The student/user being recommended
}
