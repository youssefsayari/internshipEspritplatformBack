package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.InternshipRemarkAdd;
import tn.esprit.innoxpert.Entity.InternshipRemark;
import tn.esprit.innoxpert.Service.InternshipRemarkService;

import java.util.List;

@Tag(name = "InternshipRemark Management")
@RestController
@AllArgsConstructor
@RequestMapping("/internshipRemark")
public class InternshipRemarkRestController {
    @Autowired
    InternshipRemarkService internshipRemarkService;

    @PostMapping("/add")
    public ResponseEntity<String> addInternshipRemark(@RequestBody InternshipRemarkAdd internshipRemarkDTO) {
        try {
            internshipRemarkService.addInternshipRemark(internshipRemarkDTO.getRemark(), internshipRemarkDTO.getIdInternship());
            return ResponseEntity.ok("Internship remark added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding internship remark: " + e.getMessage() + internshipRemarkDTO.getIdInternship());
        }
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteInternshipRemark(@PathVariable Long id) {
        try {
            internshipRemarkService.deleteInternshipRemark(id);
            return ResponseEntity.ok("Internship remark deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Internship remark not found: " + e.getMessage());
        }
    }

    @GetMapping("/getByInternshipId/{internshipId}")
    public ResponseEntity<List<InternshipRemark>> getInternshipRemarksByInternshipId(@PathVariable Long internshipId) {
        List<InternshipRemark> remarks = internshipRemarkService.getInternshipRemarksByInternshipId(internshipId);
        if (remarks != null && !remarks.isEmpty()) {
            return ResponseEntity.ok(remarks);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }


    @GetMapping("/getAll")
    public ResponseEntity<List<InternshipRemark>> getAllInternshipRemarks() {
        List<InternshipRemark> remarks = internshipRemarkService.getAllInternshipRemarks();
        return ResponseEntity.ok(remarks);
    }

}
