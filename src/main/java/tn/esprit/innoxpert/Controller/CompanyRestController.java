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
import tn.esprit.innoxpert.Exceptions.ResourceNotFoundException;
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
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
        try {
            companyService.removeCompanyByIdAndUserAffected(companyId);
            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "message", "Company deleted successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to delete company: " + e.getMessage()
                    ));
        }
    }

    @PutMapping("/updateCompany/{companyId}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable Long companyId,
            @RequestBody Company updatedCompany) {

        try {
            Company result = companyService.updateCompany(companyId, updatedCompany);
            return ResponseEntity.ok(result);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
    @GetMapping("/getCompanyByUserId/{userId}")
    public ResponseEntity<Company> getCompanyByUserId(@PathVariable Long userId) {
        try {
            Company company = companyService.getCompanyByUserId(userId);
            return ResponseEntity.ok(company);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

}
