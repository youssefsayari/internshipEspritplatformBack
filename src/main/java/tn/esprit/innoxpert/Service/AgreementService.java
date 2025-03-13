package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Agreement;
import tn.esprit.innoxpert.Repository.AgreementRepository;
import tn.esprit.innoxpert.Repository.InternshipRepository;
import tn.esprit.innoxpert.Repository.PostRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AgreementService implements AgreementServiceInterface {
    @Autowired
    UserRepository userRepository;
    AgreementRepository agreementRepository;
    @Override
    public List<Agreement> getAllAgreements() {
        return agreementRepository.findAll();
    }

    @Override
    public Agreement getAgreementById(Long id) {
        return agreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agreement not found with id: " + id));
    }

    @Override
    public Agreement createAgreement(Agreement agreement) {
        return agreementRepository.save(agreement);
    }

    @Override
    public Agreement updateAgreement(Long id, Agreement updatedAgreement) {
        Optional<Agreement> existingAgreement = agreementRepository.findById(id);
        if (existingAgreement.isPresent()) {
            Agreement agreement = existingAgreement.get();
            agreement.setDateDebut(updatedAgreement.getDateDebut());
            agreement.setDateFin(updatedAgreement.getDateFin());
            agreement.setRepresentative(updatedAgreement.getRepresentative());
            agreement.setUser(updatedAgreement.getUser());
            return agreementRepository.save(agreement);
        } else {
            throw new RuntimeException("Agreement not found with id: " + id);
        }
    }

    @Override
    public void deleteAgreement(Long id) {
        if (agreementRepository.existsById(id)) {
            agreementRepository.deleteById(id);
        } else {
            throw new RuntimeException("Agreement not found with id: " + id);
        }
    }
}
