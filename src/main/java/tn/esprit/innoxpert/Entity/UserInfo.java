package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idUserDetail;


    Long maxValidatedInternships;
    Long maxInternshipSupervisions;

    @OneToOne
    User user;

    @OneToMany
    @JoinColumn(name = "user_detail_id")

    List<Expertise> expertises;
}