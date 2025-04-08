package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.AddInternship;
import tn.esprit.innoxpert.Entity.Agreement;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Service.AgreementService;
import tn.esprit.innoxpert.Service.InternshipService;

import java.util.List;

@Tag(name = "Agreement Management")
@RestController
@AllArgsConstructor
@RequestMapping("/agreement")
public class AgreementContrroller {
    @Autowired
    AgreementService agreementService;

    @GetMapping("/hasApprovedInternship/{studentId}")
    public boolean hasApprovedInternship(@PathVariable Long studentId) {
        return agreementService.hasApprovedInternship(studentId);
    }

    @GetMapping("/getAllAgreements")
    public List<Agreement> getAllAgreements()
    {
        return agreementService.getAllAgreements();
    }

    @PostMapping("/addAgreement")
    public ResponseEntity<String> addAgreement (@RequestBody Long idUser)
    {
        try {
            agreementService.addAgreement(idUser);
            return ResponseEntity.ok("Internship request submitted successfully!");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @GetMapping("/getAgreementById")
    public Agreement getAgreementById(@RequestParam Long StudentId )
    {
        return agreementService.getAgreementById(StudentId);
    }
}
