package tn.esprit.innoxpert.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.innoxpert.Entity.InternshipState;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternshipResponse {
    private Long id;
    private String title;
    private String description;
    private InternshipState internshipState;
}
