package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Agreement;
import tn.esprit.innoxpert.Service.AgreementService;

import java.util.List;

@Tag(name = "Agreement Management")
@RestController
@AllArgsConstructor
@RequestMapping("/Agreement")
public class AgreementRestController {
    private final AgreementService agreementService;

    @GetMapping
    public ResponseEntity<List<Agreement>> getAllAgreements() {
        return ResponseEntity.ok(agreementService.getAllAgreements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agreement> getAgreementById(@PathVariable Long id) {
        return ResponseEntity.ok(agreementService.getAgreementById(id));
    }

    @PostMapping
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) {
        return ResponseEntity.ok(agreementService.createAgreement(agreement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agreement> updateAgreement(@PathVariable Long id, @RequestBody Agreement agreement) {
        return ResponseEntity.ok(agreementService.updateAgreement(id, agreement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAgreement(@PathVariable Long id) {
        agreementService.deleteAgreement(id);
        return ResponseEntity.ok("Agreement deleted successfully");
    }

}
