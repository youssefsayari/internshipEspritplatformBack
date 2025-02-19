package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {
    private String role;
    private String classe;

    public UserRole(String role, String classe) {
        this.role = role;
        this.classe = classe;
    }
}


