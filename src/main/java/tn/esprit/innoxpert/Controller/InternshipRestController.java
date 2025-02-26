package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.AddInternship;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.Task;
import tn.esprit.innoxpert.Service.InternshipService;

import java.util.List;

@Tag(name = "Internship Management")
@RestController
@AllArgsConstructor
@RequestMapping("/internship")
public class InternshipRestController {
    @Autowired
    InternshipService internshipService;
    @GetMapping("/getAllInternships")
    public List<Internship> getAllInternships()
    {
        return internshipService.getAllInternships();
    }
    @GetMapping("/getInternshipByCriteria")
    public List<Internship> getInternshipsByCriteria(
            @RequestParam(required = false) Long idUser,
            @RequestParam(required = false) Long idPost) {
        List<Internship> internships = internshipService.getInternshipsByCriteria(idUser, idPost);
        return internships;
    }
    @GetMapping("/getInternshipById")
    public Internship getInternshipById(@RequestParam Long internshipId )
    {
        return internshipService.getInternshipById(internshipId);
    }
    @PostMapping("/addInternship")
    public ResponseEntity<String> addTask (@RequestBody AddInternship addInternship)
    {
        internshipService.addInternship(addInternship);
        return ResponseEntity.ok("Internship request submitted successfully!");
    }

    @DeleteMapping("/removeInternshipById/{internshipId}")
    public void removeInternshipById(@PathVariable ("internshipId") Long internshipId)
    {
        internshipService.removeInternshipById(internshipId);
    }

    @PutMapping("/updateInternship")

    public Internship updateInternship(@RequestBody Internship internship)
    {
        return internshipService.updateInternship(internship);
    }


}
