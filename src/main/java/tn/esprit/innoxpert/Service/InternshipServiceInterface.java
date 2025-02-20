package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Internship;

import java.util.List;

public interface InternshipServiceInterface {
    public List<Internship> getAllInternships();
    public Internship getInternshipById(Long internshipId);
    public Internship addInternship(Internship b);
    public void removeInternshipById(Long internshipId);
    public Internship updateInternship (Internship b );
    public Internship approveInternship(Internship b);
    public Internship approveInternshipById(Long internshipId) ;
}
