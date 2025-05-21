package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.AgreementRemark;
import tn.esprit.innoxpert.Entity.InternshipRemark;

import java.util.List;

public interface AgreementRemarkServiceInterface {
    public void addAgreementRemark(String remark, Long idInternship);
    public void deleteAgreementRemark(Long id);
    public List<AgreementRemark> getAgreementRemarksByAgreementId(Long internshipId);
    public List<AgreementRemark> getAllAgreementRemarks();
    public void removeAgreementRemarkByAgreemntID(Long id);
}
