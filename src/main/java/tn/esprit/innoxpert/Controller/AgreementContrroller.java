package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.innoxpert.DTO.AddInternship;
import tn.esprit.innoxpert.DTO.AgreementDTO;
import tn.esprit.innoxpert.DTO.AgreementRequestDTO;
import tn.esprit.innoxpert.DTO.InternshipDetailsDTO;
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

    @GetMapping("/getInternshipsForStudent/{studentId}")
    public List<InternshipDetailsDTO> getInternshipsForStudent(@PathVariable Long studentId) {
        return agreementService.getInternshipsForStudent(studentId);
    }
    @GetMapping("/getAllAgreements")
    public List<Agreement> getAllAgreements()
    {
        return agreementService.getAllAgreements();
    }

    @PostMapping("/addAgreement")
    public ResponseEntity<String> addAgreement(@RequestBody AgreementRequestDTO agreementRequestDTO) {
        try {
            agreementService.addAgreement(agreementRequestDTO);
            return ResponseEntity.ok("Internship request submitted successfully!");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping("/getAgreementById")
    public AgreementDTO getAgreementById(@RequestParam Long studentId) {
        AgreementDTO agreementDTO = agreementService.getAgreementById(studentId);
        if (agreementDTO != null) {
            return agreementDTO;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agreement not found for studentId: " + studentId);
        }
    }


}
