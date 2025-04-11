package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.innoxpert.Entity.TypeAgreement;

@Getter
@Setter
public class TimeLineResponse {
    private Long id;
    private String title;
    private String description;
    private String dateLimite;
    private Long studentId;
    private Long documentId;
    private TypeAgreement timeLaneState;
}
