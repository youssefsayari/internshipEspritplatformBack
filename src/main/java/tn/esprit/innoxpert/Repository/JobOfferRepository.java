package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.innoxpert.Entity.JobOffer;

import java.util.List;

public interface JobOfferRepository extends JpaRepository<JobOffer,Long> {
    List<JobOffer> findByCompanyId(Long companyId);
    List<JobOffer> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

}
