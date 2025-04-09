package tn.esprit.innoxpert.DTO;

import tn.esprit.innoxpert.Entity.TypeAgreement;

import java.util.Date;

public class AgreementDTO {
    private Long id;
    private Date startDate;
    private Date endDate;
    private String companyName;
    private String companyRepresentative;
    private TypeAgreement agreementState;

    public AgreementDTO(Long id, Date startDate, Date endDate, String companyName, String companyRepresentative, TypeAgreement agreementState) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.companyName = companyName;
        this.companyRepresentative = companyRepresentative;
        this.agreementState = agreementState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyRepresentative() {
        return companyRepresentative;
    }

    public void setCompanyRepresentative(String companyRepresentative) {
        this.companyRepresentative = companyRepresentative;
    }

    public TypeAgreement getAgreementState() {
        return agreementState;
    }

    public void setAgreementState(TypeAgreement agreementState) {
        this.agreementState = agreementState;
    }
}
