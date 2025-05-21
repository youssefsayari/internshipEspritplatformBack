package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddInternship {
    Long idUser;
    Long idPost;
    public AddInternship(Long idUser, Long idPost) {
        this.idUser = idUser;
        this.idPost = idPost;
    }
}
