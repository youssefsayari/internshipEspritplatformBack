package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "startDate est obligatoire.")
    Date startDate;

    @NotNull(message = "endDate est obligatoire.")
    Date endDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "L'état du agreement est obligatoire.")
    TypeAgreement agreementState;

    @NotNull(message = "postId est obligatoire.")
    Long postId;

    @NotNull(message = "creationDate est obligatoire.")
    Date creationDate;

    @OneToOne
    @JoinColumn(name = "student_id_user")
    private User student;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
