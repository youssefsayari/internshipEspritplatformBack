package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Repository.InternshipRepository;

import java.util.List;
@Service
@AllArgsConstructor
public class InternshipService implements InternshipServiceInterface {
    @Autowired
    InternshipRepository internshipRepository;

    @Override
    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    @Override
    public Internship getInternshipById(Long internshipId) {
        return internshipRepository.findById(internshipId).orElse(null);
    }

    @Override
    public Internship addInternship(Internship b) {
        return null;
    }

    @Override
    public void removeInternshipById(Long internshipId) {
        internshipRepository.deleteById(internshipId);
    }

    @Override
    public Internship updateInternship(Internship b) {
        return null;
    }

    @Override
    public Internship approveInternship(Internship b) {
        return null;
    }

    @Override
    public Internship approveInternshipById(Long internshipId) {
        return null;
    }
}
