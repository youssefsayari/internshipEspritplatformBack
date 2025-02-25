package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.DTO.AddInternship;
import tn.esprit.innoxpert.Entity.Internship;

import java.util.List;
import java.util.Map;

public interface InternshipServiceInterface {

    public List<Internship> getAllInternships();
    public List<Internship> getInternshipsByCriteria(Long idUser,Long idPost);
    public Internship getInternshipById(Long internshipId);
    public Internship addInternship(AddInternship addInternship);
    public void removeInternshipById(Long internshipId);
    public Internship updateInternship (Internship b);
    public Internship approveInternship(Long internshipId);
    public Internship rejectInternship(Long internshipId);
    public Map<String, Object> getInternshipStatistics();
    public Internship affectationTutor(Long internshipId);
    public Internship GenerateInternshipcertificate (Long interbshipId);

}
