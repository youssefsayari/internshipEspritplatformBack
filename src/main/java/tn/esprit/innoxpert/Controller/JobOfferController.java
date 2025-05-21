package tn.esprit.innoxpert.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.JobOffer;
import tn.esprit.innoxpert.Service.JobOfferService;
import tn.esprit.innoxpert.Service.JobOfferServiceInterface;

import java.util.List;

@RestController
@RequestMapping("/api/joboffers")
@RequiredArgsConstructor
public class JobOfferController {

    private final JobOfferServiceInterface jobOfferService;

    // 🔹 Create a job offer
    @PostMapping("/create/{companyId}")
    public ResponseEntity<JobOffer> createJobOffer(@RequestBody JobOffer jobOffer, @PathVariable Long companyId) {
        JobOffer created = jobOfferService.createJobOffer(jobOffer, companyId);
        return ResponseEntity.ok(created);
    }

    // 🔹 Get all job offers
    @GetMapping("/all")
    public ResponseEntity<List<JobOffer>> getAllJobOffers() {
        return ResponseEntity.ok(jobOfferService.getAllJobOffers());
    }

    // 🔹 Get a job offer by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobOffer> getJobOfferById(@PathVariable Long id) {
        return jobOfferService.getJobOfferById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Get job offers by company ID
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobOffer>> getJobOffersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(jobOfferService.getJobOffersByCompany(companyId));
    }

    // 🔹 Search job offers by keyword
    @GetMapping("/search")
    public ResponseEntity<List<JobOffer>> searchJobOffers(@RequestParam String keyword) {
        return ResponseEntity.ok(jobOfferService.searchJobOffers(keyword));
    }

    // 🔹 Update a job offer
    @PutMapping("/update/{id}")
    public ResponseEntity<JobOffer> updateJobOffer(@PathVariable Long id, @RequestBody JobOffer jobOffer) {
        return ResponseEntity.ok(jobOfferService.updateJobOffer(id, jobOffer));
    }

    // 🔹 Delete a job offer
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }
}
