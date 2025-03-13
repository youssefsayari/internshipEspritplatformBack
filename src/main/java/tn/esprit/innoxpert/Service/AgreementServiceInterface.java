package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Agreement;

import java.util.List;

public interface AgreementServiceInterface {
    List<Agreement> getAllAgreements();
    Agreement getAgreementById(Long id);
    Agreement createAgreement(Agreement agreement);
    Agreement updateAgreement(Long id, Agreement agreement);
    void deleteAgreement(Long id);
}
