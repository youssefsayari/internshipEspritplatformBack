package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AgreementRemark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String remark;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "agreement_id")
    @JsonIgnore
    private Agreement agreement;

}
