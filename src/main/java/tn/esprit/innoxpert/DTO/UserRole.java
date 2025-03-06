package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {
    private String role;
    private String classe;
    private Long id;

    public UserRole(String role, String classe, Long id) {
        this.role = role;
        this.classe = classe;
        this.id = id;
    }
}


