package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.InternshipRemark;

import java.util.List;

public interface InternshipRemarkServiceInterface {
    public void addInternshipRemark(String remark, Long idInternship);
    public void deleteInternshipRemark(Long id);
    public List<InternshipRemark> getInternshipRemarksByInternshipId(Long internshipId);
    public List<InternshipRemark> getAllInternshipRemarks();
}
