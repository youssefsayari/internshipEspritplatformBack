package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Agreement;
import tn.esprit.innoxpert.Entity.AgreementRemark;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.InternshipRemark;
import tn.esprit.innoxpert.Repository.AgreementRemarkRepository;
import tn.esprit.innoxpert.Repository.AgreementRepository;
import tn.esprit.innoxpert.Repository.InternshipRemarkRepository;
import tn.esprit.innoxpert.Repository.InternshipRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AgreementRemarkService implements AgreementRemarkServiceInterface {
    @Autowired
    AgreementRemarkRepository agreementRemarkRepository;
    AgreementRepository agreementRepository;
    @Override
    public void addAgreementRemark(String remark, Long idAgreement) {
        Agreement agreement = agreementRepository.findById(idAgreement)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        AgreementRemark agreementRemark = new AgreementRemark();
        agreementRemark.setRemark(remark);
        agreementRemark.setAgreement(agreement);

        agreementRemarkRepository.save(agreementRemark);
    }

    @Override
    public void deleteAgreementRemark(Long id) {
        agreementRemarkRepository.deleteById(id);
    }


    @Override
    public List<AgreementRemark> getAgreementRemarksByAgreementId(Long idAgreement) {
        return agreementRemarkRepository.findByAgreement_Id(idAgreement);
    }

    @Override
    public List<AgreementRemark> getAllAgreementRemarks() {
        return agreementRemarkRepository.findAll();
    }

    @Override
    public void removeAgreementRemarkByAgreemntID(Long agreementId) {
        agreementRemarkRepository.deleteByAgreementId(agreementId);
    }
}
