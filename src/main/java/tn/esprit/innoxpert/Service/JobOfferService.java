package tn.esprit.innoxpert.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.JobOffer;
import tn.esprit.innoxpert.Repository.CompanyRepository;
import tn.esprit.innoxpert.Repository.JobOfferRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobOfferService implements JobOfferServiceInterface{
    private final JobOfferRepository jobOfferRepository;
    private final CompanyRepository companyRepository;

    @Override
    public JobOffer createJobOffer(JobOffer jobOffer, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        jobOffer.setCompany(company);
        return jobOfferRepository.save(jobOffer);
    }

    @Override
    public JobOffer updateJobOffer(Long id, JobOffer updatedOffer) {
        JobOffer existing = jobOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job offer not found"));

        existing.setTitle(updatedOffer.getTitle());
        existing.setDescription(updatedOffer.getDescription());
        existing.setLocation(updatedOffer.getLocation());
        existing.setPublishDate(updatedOffer.getPublishDate());
        existing.setDeadline(updatedOffer.getDeadline());
        existing.setSalary(updatedOffer.getSalary());
        existing.setJobType(updatedOffer.getJobType());

        return jobOfferRepository.save(existing);
    }

    @Override
    public void deleteJobOffer(Long id) {
        jobOfferRepository.deleteById(id);
    }

    @Override
    public Optional<JobOffer> getJobOfferById(Long id) {
        return jobOfferRepository.findById(id);
    }

    @Override
    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    @Override
    public List<JobOffer> getJobOffersByCompany(Long companyId) {
        return jobOfferRepository.findByCompanyId(companyId);
    }

    @Override
    public List<JobOffer> searchJobOffers(String keyword) {
        return jobOfferRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }
}
