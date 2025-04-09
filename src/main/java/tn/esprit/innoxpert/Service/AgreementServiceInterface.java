package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.DTO.AgreementDTO;
import tn.esprit.innoxpert.DTO.AgreementRequestDTO;
import tn.esprit.innoxpert.DTO.InternshipDetailsDTO;
import tn.esprit.innoxpert.Entity.Agreement;

import java.util.List;

public interface AgreementServiceInterface {
    public List<Agreement> getAllAgreements();
    AgreementDTO getAgreementById(Long studentId);
    void addAgreement(AgreementRequestDTO agreementRequestDTO);
    boolean hasApprovedInternship(Long studentId);
    List<InternshipDetailsDTO> getInternshipsForStudent(Long studentId);


}
