package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Service.CompanyService;

import java.util.List;

@Tag(name = "Company Management", description = "Endpoints for managing companies")
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyRestController {

    private final CompanyService companyService;

    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/getCompanyById/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long companyId) {
        Company company = companyService.getCompanyById(companyId);
        return company != null ? ResponseEntity.ok(company) : ResponseEntity.notFound().build();
    }

    @PostMapping("/addCompany")
    public ResponseEntity<Company> addCompanyAndAssignUser(@RequestBody Company company) {
        Company createdCompany = companyService.addCompanyAndAffectToNewUser(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @Transactional
    @DeleteMapping("/deleteCompany/{companyId}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable Long companyId) {
        if (companyService.getCompanyById(companyId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found");
        }
        companyService.removeCompanyByIdAndUserAffected(companyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Company deleted successfully");
    }

    @PutMapping("/updateCompany/{companyId}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long companyId, @RequestBody Company updatedCompany) {
        Company existingCompany = companyService.getCompanyById(companyId);
        if (existingCompany == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        updatedCompany.setId(companyId);
        return ResponseEntity.ok(companyService.updateCompany(updatedCompany));
    }

    @GetMapping("/getCompanyFollowers/{companyId}")
    public ResponseEntity<List<User>> getCompanyFollowers(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyFollowers(companyId));
    }

    @PutMapping("/{companyId}/follow/{userId}")
    public ResponseEntity<String> followCompany(@PathVariable Long userId, @PathVariable Long companyId) {
        try {
            companyService.followCompany(userId, companyId);
            return ResponseEntity.ok("User " + userId + " is now following Company " + companyId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{companyId}/unfollow/{userId}")
    public ResponseEntity<String> unfollowCompany(@PathVariable Long userId, @PathVariable Long companyId) {
        try {
            companyService.unfollowCompany(userId, companyId);
            return ResponseEntity.ok("User " + userId + " has unfollowed Company " + companyId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
