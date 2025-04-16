package tn.esprit.innoxpert.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Reclamation;
import tn.esprit.innoxpert.Service.ReclamationService;

import java.util.List;

@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;

    @PostMapping("/{userId}")
    public Reclamation create(@RequestBody Reclamation r,@PathVariable Long userId) {
        return reclamationService.createReclamation(r,userId);
    }

    @GetMapping
    public List<Reclamation> getAll() {
        return reclamationService.getAllReclamations();
    }

    @GetMapping("/{id}")
    public Reclamation getById(@PathVariable Long id) {
        return reclamationService.getReclamationById(id)
                .orElseThrow(() -> new RuntimeException("Reclamation not found"));
    }

    @PutMapping("/{id}")
    public Reclamation update(@PathVariable Long id, @RequestBody Reclamation r) {
        return reclamationService.updateReclamation(id, r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reclamationService.deleteReclamation(id);
    }

    @PutMapping("/{id}/assign/{userId}")
    public Reclamation assign(@PathVariable Long id, @PathVariable Long userId) {
        return reclamationService.assignReclamation(id, userId);
    }

    @PostMapping("/{id}/respond")
    public Reclamation respondToReclamation(@PathVariable Long id, @RequestBody String response) {
        return reclamationService.respondToReclamation(id, response);
    }

    @PostMapping("/{id}/reject")
    public Reclamation rejectResponse(@PathVariable Long id) {
        return reclamationService.rejectResponse(id);
    }

    @PostMapping("/{id}/{userId}/progress")
    public Reclamation progressResponse(@PathVariable Long id, @PathVariable Long userId) {
        return reclamationService.inProgressResponse(id,userId);
    }

}
