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
public class InternshipRemark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String remark;

    @ManyToOne
    @JoinColumn(name = "internship_id")
    @JsonIgnore
    private Internship internship;

}
