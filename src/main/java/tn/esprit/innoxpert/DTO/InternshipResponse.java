package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternshipResponse {
    private Long id;
    private String title;
    private String description;
    private String internshipState;
}
