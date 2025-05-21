package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest {
    private String identifiant;
    private String password;
}