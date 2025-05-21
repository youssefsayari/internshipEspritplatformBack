package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {
    private String role;
    private String classe;
    private Long id;
    private String email;

    public UserRole(String role, String classe, Long id, String email) {
        this.role = role;
        this.classe = classe;
        this.id = id;
        this.email = email;
    }
}


