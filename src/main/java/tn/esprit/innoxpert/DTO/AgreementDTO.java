package tn.esprit.innoxpert.DTO;

import lombok.Getter;
import lombok.Setter;
import tn.esprit.innoxpert.Entity.TypeAgreement;

import java.util.Date;
@Getter
@Setter
public class AgreementDTO {
    private Long id;
    private Date startDate;
    private Date endDate;
    private String companyName;
    private String companyRepresentative;
    private TypeAgreement agreementState;
    private Date creationDate;
    private String validatorName;
    private Long validateurId;


    public AgreementDTO(Long id, Date startDate, Date endDate, String companyName, String companyRepresentative, TypeAgreement agreementState, Date creationDate, String validatorName, Long validateurId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.companyName = companyName;
        this.companyRepresentative = companyRepresentative;
        this.agreementState = agreementState;
        this.creationDate = creationDate;
        this.validatorName = validatorName;
        this.validateurId = validateurId;
    }
}
