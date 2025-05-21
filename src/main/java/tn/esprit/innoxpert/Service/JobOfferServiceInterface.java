package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.JobOffer;

import java.util.List;
import java.util.Optional;

public interface JobOfferServiceInterface {
    JobOffer createJobOffer(JobOffer jobOffer, Long companyId);
    JobOffer updateJobOffer(Long id, JobOffer updatedOffer);
    void deleteJobOffer(Long id);
    Optional<JobOffer> getJobOfferById(Long id);
    List<JobOffer> getAllJobOffers();
    List<JobOffer> getJobOffersByCompany(Long companyId);
    List<JobOffer> searchJobOffers(String keyword);
}
