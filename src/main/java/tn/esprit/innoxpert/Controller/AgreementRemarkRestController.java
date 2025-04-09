package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.AgreementRemarkAdd;
import tn.esprit.innoxpert.DTO.InternshipRemarkAdd;
import tn.esprit.innoxpert.Entity.AgreementRemark;
import tn.esprit.innoxpert.Entity.InternshipRemark;
import tn.esprit.innoxpert.Service.AgreementRemarkService;
import tn.esprit.innoxpert.Service.InternshipRemarkService;

import java.util.List;

@Tag(name = "AgreementRemark Management")
@RestController
@AllArgsConstructor
@RequestMapping("/agreementRemark")
public class AgreementRemarkRestController {
    @Autowired
    AgreementRemarkService agreementRemarkService;

    @PostMapping("/add")
    public ResponseEntity<String> addAgreementRemark(@RequestBody AgreementRemarkAdd agreementRemarkDTO) {
        try {
            agreementRemarkService.addAgreementRemark(agreementRemarkDTO.getRemark(), agreementRemarkDTO.getIdAgreement());
            return ResponseEntity.ok("Agreement remark added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding agreement remark: " + e.getMessage() + agreementRemarkDTO.getIdAgreement());
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAgreementRemark(@PathVariable Long id) {
        try {
            agreementRemarkService.deleteAgreementRemark(id);
            return ResponseEntity.ok("Agreement remark deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Agreement remark not found: " + e.getMessage());
        }
    }

    @GetMapping("/getByAgreementId/{agreementId}")
    public ResponseEntity<List<AgreementRemark>> getAgreementRemarksByAgreementId(@PathVariable Long AgreementId) {
        List<AgreementRemark> remarks = agreementRemarkService.getAgreementRemarksByAgreementId(AgreementId);
        if (remarks != null && !remarks.isEmpty()) {
            return ResponseEntity.ok(remarks);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<AgreementRemark>> getAllAgreementRemarks() {
        List<AgreementRemark> remarks = agreementRemarkService.getAllAgreementRemarks();
        return ResponseEntity.ok(remarks);
    }
}
