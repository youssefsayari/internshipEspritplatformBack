package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Reclamation;
import tn.esprit.innoxpert.Entity.ReclamationStatus;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Repository.ReclamationRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReclamationService implements ReclamationServiceInterface {

    @Autowired
    private final ReclamationRepository reclamationRepository;

    @Autowired
    private final HistoryLogService historyLogService;

    @Autowired
    private final UserService userService;

    @Override
    public Reclamation createReclamation(Reclamation reclamation,Long userId ) {
        Reclamation saved = reclamationRepository.save(reclamation);
        User user = userService.getUserById(userId);
        saved.setCreatedBy(user);
        historyLogService.log("Création", "Nouvelle réclamation créée avec sujet : " + saved.getSubject());
        return saved;
    }

    @Override
    public Reclamation updateReclamation(Long id, Reclamation updated) {
        Reclamation existing = reclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reclamation not found"));
        existing.setSubject(updated.getSubject());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        existing.setResponse(updated.getResponse());
        existing.setRejectedByUser(updated.isRejectedByUser());

        Reclamation saved = reclamationRepository.save(existing);
        historyLogService.log("Mise à jour", "Réclamation ID " + saved.getId() + " mise à jour.");
        return saved;
    }

    @Override
    public void deleteReclamation(Long id) {
        reclamationRepository.deleteById(id);
        historyLogService.log("Suppression", "Réclamation supprimée (ID = " + id + ")");
    }

    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public Optional<Reclamation> getReclamationById(Long id) {
        return reclamationRepository.findById(id);
    }

    @Override
    public Reclamation assignReclamation(Long reclamationId, Long userId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Reclamation not found"));
        reclamation.setStatus(ReclamationStatus.IN_PROGRESS);
        Reclamation saved = reclamationRepository.save(reclamation);
        historyLogService.log("Affectation", "Réclamation " + reclamationId + " assignée à l'utilisateur ID " + userId);
        return saved;
    }

    @Override
    public Reclamation respondToReclamation(Long reclamationId, String response) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Reclamation not found"));
        reclamation.setResponse(response);
        reclamation.setStatus(ReclamationStatus.RESOLVED);
        reclamation.setAdminResponded(true);
        Reclamation saved = reclamationRepository.save(reclamation);
        historyLogService.log("Réponse admin", "Réclamation " + reclamationId + " a été résolue. Réponse : " + response);
        return saved;
    }

    @Override
    public Reclamation rejectResponse(Long reclamationId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Reclamation not found"));

        if (reclamation.getStatus() == ReclamationStatus.PENDING) {
            reclamation.setStatus(ReclamationStatus.REJECTED);
            reclamation.setRejectedByUser(true);
            historyLogService.log(" Rejet utilisateur", "Réclamation " + reclamationId + " rejetée par l'utilisateur ");
        } else {
            throw new IllegalStateException("Réclamation non résolue, impossible de rejeter.");
        }

        return reclamationRepository.save(reclamation);


    }
@Override
    public Reclamation inProgressResponse(Long reclamationId, Long userId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Reclamation not found"));

        if (reclamation.getStatus() == ReclamationStatus.PENDING) {
            reclamation.setStatus(ReclamationStatus.IN_PROGRESS);
            User user = userService.getUserById(userId);
            reclamation.setAdminPriseEnCharge(user);
            historyLogService.log(" Progress", "Réclamation " + reclamationId + " in progress par l'utilisateur ");
        } else {
            throw new IllegalStateException("Réclamation non résolue, impossible de rejeter.");
        }

        return reclamationRepository.save(reclamation);


    }
}
