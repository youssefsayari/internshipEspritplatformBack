package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Agreement;

import java.util.List;

public interface AgreementServiceInterface {
    public List<Agreement> getAllAgreements();
    public Agreement getAgreementById(Long studentId);
    public void addAgreement(Long idStudent);
    boolean hasApprovedInternship(Long studentId);



}
