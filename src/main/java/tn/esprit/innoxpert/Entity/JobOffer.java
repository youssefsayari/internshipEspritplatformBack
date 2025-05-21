package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    String location;

    Double salary;

    @Enumerated(EnumType.STRING)
    TypeJob jobType; // e.g. FULL_TIME, PART_TIME, INTERNSHIP, etc.

    LocalDate publishDate;
    LocalDate deadline;

    boolean isRemote;

    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;

    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Application> applications;

    @ElementCollection
    List<String> requiredSkills;
}
