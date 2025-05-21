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
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    LocalDate applicationDate;

    @Enumerated(EnumType.STRING)
    TypeApplicationStatus status;

    @Column(columnDefinition = "TEXT")
    String motivationLetter;

    String resumeUrl; // or you can relate to a Document

    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    JobOffer jobOffer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User applicant;
}
