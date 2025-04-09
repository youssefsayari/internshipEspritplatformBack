package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    @PostMapping("/approveAgreement/{AgreementId}")
    public ResponseEntity<String> approveAgreement(@PathVariable Long AgreementId) {
        try {
            agreementService.approveAgreement(AgreementId);
            return ResponseEntity.ok("Agreement accepted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/acceptAgreement/{AgreementId}")
    public ResponseEntity<String> acceptAgreement(@PathVariable Long AgreementId) {
        try {
            agreementService.acceptAgreement(AgreementId);
            return ResponseEntity.ok("Agreement approved successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/rejectAgreement/{AgreementId}")
    public ResponseEntity<String> rejectAgreement(@PathVariable Long AgreementId) {
        try {
            agreementService.rejectAgreement(AgreementId);
            return ResponseEntity.ok("Agreement application denied!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/download-pdf/{id}")
    public ResponseEntity<Resource> downloadAgreementPDF(@PathVariable Long id) throws IOException {

        String userDesktopPath = System.getProperty("user.home") + "/Desktop/";
        String fileName = "Convention_Stage_" + id + ".pdf";
        String fullPath = userDesktopPath + fileName;

        agreementService.GeneretePDF(id);

        File file = new File(fullPath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(file.length())
                .body(resource);
    }


}
