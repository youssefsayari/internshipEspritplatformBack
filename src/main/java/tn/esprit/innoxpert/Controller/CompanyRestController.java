package tn.esprit.innoxpert.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.Image;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Service.CloudinaryService;
import tn.esprit.innoxpert.Service.CompanyService;
import tn.esprit.innoxpert.Service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Tag(name = "Company Management", description = "Endpoints for managing companies")
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyRestController {

    private final CompanyService companyService;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;



    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/getCompanyById/{companyId}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long companyId) {
        Company company = companyService.getCompanyById(companyId);
        return company != null ? ResponseEntity.ok(company) : ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCompany(
            @RequestPart("company") String companyJson,
            @RequestPart("file") MultipartFile file) throws IOException {

        // Convertir le JSON en objet Company
        ObjectMapper objectMapper = new ObjectMapper();
        Company company = objectMapper.readValue(companyJson, Company.class);

        // Vérifier si la validation échoue
        Set<ConstraintViolation<Company>> violations = Validation.buildDefaultValidatorFactory()
                .getValidator().validate(company);

        if (!violations.isEmpty()) {
            List<String> errors = violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Company createdCompany = companyService.addCompanyAndAffectToNewUser(company, file);
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

    @GetMapping("/getCompanyIdByUserId/company/{userId}")
    public Long getCompanyIdByUserId( @PathVariable Long userId) {
        return companyService.getCompanyIdByUserId(userId);
    }

    @GetMapping("/IsCompany/company/{userId}")
    public Boolean IsCompany( @PathVariable Long userId) {
        return companyService.IsCompany(userId);
    }

}
