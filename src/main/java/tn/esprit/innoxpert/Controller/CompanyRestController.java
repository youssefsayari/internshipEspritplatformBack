package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(companyService.addCompanyAndAffectToNewUser(company));
    }

    @DeleteMapping("/deleteCompany/{companyId}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable Long companyId) {
        companyService.removeCompanyByIdAndUserAffected(companyId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateCompany/{companyId}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long companyId, @RequestBody Company updatedCompany) {
        updatedCompany.setId(companyId);
        return ResponseEntity.ok(companyService.updateCompany(updatedCompany));
    }

    @GetMapping("/getCompanyFollowers/{companyId}")
    public ResponseEntity<List<User>> getCompanyFollowers(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyFollowers(companyId));
    }
}
