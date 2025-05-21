package tn.esprit.innoxpert.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternshipAdminResponse {
    private Long idInternship;
    private Long idTutor;
    private Long idStudent;
    private String studentName;
    private String classe;
    private String tutorName;
    private String internshipState;
    private Long validator_id;
    private String validatorName;
}
