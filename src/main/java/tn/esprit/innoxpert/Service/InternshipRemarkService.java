package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.InternshipRemark;
import tn.esprit.innoxpert.Repository.InternshipRemarkRepository;
import tn.esprit.innoxpert.Repository.InternshipRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class InternshipRemarkService implements InternshipRemarkServiceInterface {
    @Autowired
    InternshipRemarkRepository internshipRemarkRepository;
    InternshipRepository internshipRepository;

    @Override
    public void addInternshipRemark(String remark, Long idInternship) {
        Internship internship = internshipRepository.findById(idInternship)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        InternshipRemark internshipRemark = new InternshipRemark();
        internshipRemark.setRemark(remark);
        internshipRemark.setInternship(internship);

        internshipRemarkRepository.save(internshipRemark);
    }


    @Override
    public void deleteInternshipRemark(Long id) {
        internshipRemarkRepository.deleteById(id);
    }

    @Override
    public List<InternshipRemark> getInternshipRemarksByInternshipId(Long internshipId) {
        return internshipRemarkRepository.findByInternship_Id(internshipId);
    }


    @Override
    public List<InternshipRemark> getAllInternshipRemarks() {
        return internshipRemarkRepository.findAll();
    }
}
