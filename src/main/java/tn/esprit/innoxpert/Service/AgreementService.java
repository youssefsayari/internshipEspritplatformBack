package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Agreement;
import tn.esprit.innoxpert.Entity.InternshipState;
import tn.esprit.innoxpert.Repository.AgreementRepository;
import tn.esprit.innoxpert.Repository.InternshipRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AgreementService implements AgreementServiceInterface {
    @Autowired
    AgreementRepository agreementRepository;
    InternshipRepository internshipRepository;

    @Override
    public List<Agreement> getAllAgreements() {
        return List.of();
    }

    @Override
    public Agreement getAgreementById(Long studentId) {
        return null;
    }

    @Override
    public void addAgreement(Long idStudent) {

    }

    @Override
    public boolean hasApprovedInternship(Long studentId) {
        return internshipRepository.existsByStudentIdAndState(studentId, InternshipState.APPROVED);
    }

}
