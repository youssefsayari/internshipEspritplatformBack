package tn.esprit.innoxpert.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.DTO.CompanyAnalyticsDto;
import tn.esprit.innoxpert.DTO.PartnershipRequest;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.Image;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.ImageProcessingException;
import tn.esprit.innoxpert.Exceptions.ResourceNotFoundException;
import tn.esprit.innoxpert.Service.CloudinaryService;
import tn.esprit.innoxpert.Service.CompanyService;
import tn.esprit.innoxpert.Service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.TypeSector;
import tn.esprit.innoxpert.Util.CompanyDataEnricher;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Company Management", description = "Endpoints for managing companies")
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyRestController {

    private final CompanyService companyService;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;
    private final CompanyDataEnricher companyDataEnricher;




    @GetMapping("/getAllCompanies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/getCompanyById/{companyId}")
    @Operation(summary = "Get company by ID", description = "Returns a single company by its ID")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long companyId) {
        if (companyId == null || companyId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        Company company = companyService.getCompanyById(companyId);
        return company != null
                ? ResponseEntity.ok(company)
                : ResponseEntity.notFound().build();
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

    @DeleteMapping("/deleteCompany/{companyId}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
        try {
            companyService.removeCompanyByIdAndUserAffected(companyId);
            return ResponseEntity.ok().body(Map.of(
                    "success", true,
                    "message", "Company deleted successfully"
            ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
        } catch (Exception e) {
            String errorMessage = "Failed to delete company: " + e.getMessage();
            System.err.println(errorMessage);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "message", errorMessage,
                            "errorDetails", e.getClass().getSimpleName()
                    ));
        }
    }


    @PutMapping("/updateCompany/{companyId}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable Long companyId,
            @RequestBody Company updatedCompany) {

        System.out.println("Received update for company " + companyId + ": " + updatedCompany);

        try {
            Company result = companyService.updateCompany(companyId, updatedCompany);
            return ResponseEntity.ok(result);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Update failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getCompanyFollowers/{companyId}")
    public ResponseEntity<List<User>> getCompanyFollowers(@PathVariable Long companyId) {
        return ResponseEntity.ok(companyService.getCompanyFollowers(companyId));
    }
    @GetMapping("/getCompaniesFollowedByUser/{userId}")
    public ResponseEntity<List<Company>> getCompaniesFollowedByUser(@PathVariable Long userId) {
        try {
            List<Company> followedCompanies = companyService.getCompaniesFollowedByUser(userId);
            return ResponseEntity.ok(followedCompanies);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
    @GetMapping("/isFollowing/{companyId}/{userId}")
    public ResponseEntity<Boolean> isFollowingCompany(
            @PathVariable Long userId,
            @PathVariable Long companyId) {
        boolean isFollowing = companyService.isUserFollowingCompany(userId, companyId);
        return ResponseEntity.ok(isFollowing);
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
    /*------------------------api auto form add company completion---------------------------------------------*/
    @Value("${pdl.api.key}")
    private String pdlApiKey;
    @GetMapping("/api/autocomplete/enrich")
    public ResponseEntity<?> enrichCompany(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String website) {

        try {
            Company company = companyDataEnricher.enrichCompanyData(name, website);
            // Assurez-vous que numEmployees a une valeur par défaut si null
            if (company.getNumEmployees() == null) {
                company.setNumEmployees(10); // Valeur par défaut
            }
            return ResponseEntity.ok(company);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Erreur lors de l'enrichissement: " + e.getMessage());
        }
    }
    /*----------------------------------------------------------------------*/
// CompanyRestController.java
    @GetMapping("/analytics")
    public ResponseEntity<List<CompanyAnalyticsDto>> getCompaniesAnalytics() {
        try {
            List<CompanyAnalyticsDto> analytics = companyService.getAllCompaniesWithAnalytics();
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // In CompanyRestController.java
    @PostMapping("/sendPartnershipEmail")
    public ResponseEntity<?> sendPartnershipEmail(@RequestBody PartnershipRequest request) {
        System.out.println("Requête reçue : " + request.getEmail() + " | " + request.getMessage());
        try {
            companyService.sendPartnershipEmail(request.getEmail(), request.getMessage());
            return ResponseEntity.ok().body(Map.of("success", true, "message", "Email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Failed to send email: " + e.getMessage()));
        }
    }
}
