package tn.esprit.innoxpert.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.innoxpert.Entity.Skill;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternshipTutorResponse {
    private Long idInternship;
    private String studentName;
    private String classe;
    private String internshipState;
    private String typeInternship;
    private String title;
    private String content;
    private String companyName;
    List<Skill> skills;
}
