package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.DTO.AddInternship;
import tn.esprit.innoxpert.DTO.InternshipAdminResponse;
import tn.esprit.innoxpert.DTO.InternshipResponse;
import tn.esprit.innoxpert.DTO.InternshipTutorResponse;
import tn.esprit.innoxpert.Entity.Internship;

import java.util.List;
import java.util.Map;

public interface InternshipServiceInterface {

    public List<Internship> getAllInternships();
    public List<InternshipResponse> getInternshipsByCriteria(Long idUser, Long idPost);
    public List<InternshipAdminResponse> getInternshipsForAdmin(Long idPost);
    public Internship getInternshipById(Long internshipId);
    public void addInternship(AddInternship addInternship);
    public void removeInternshipById(Long internshipId);
    public Internship updateInternship (Internship b);
    public void affectationValidator(Long internshipId, Long tutorId);
    public void approveInternship(Long internshipId);
    public void rejectInternship(Long internshipId);
    public List<InternshipTutorResponse> getInternshipsForTutor(Long idUser);
    public Map<String, Object> getInternshipStatistics();
    public Internship GenerateInternshipcertificate (Long interbshipId);

}
