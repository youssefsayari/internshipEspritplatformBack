package tn.esprit.innoxpert.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String classe;
    private String nameTutor;
    private Long idTutor;
    private Long maxValidatedInternships;
    private Long maxInternshipSupervisions;
    private String expertise;

}
