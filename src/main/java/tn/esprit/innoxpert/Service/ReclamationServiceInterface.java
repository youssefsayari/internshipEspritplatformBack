package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Reclamation;

import java.util.List;
import java.util.Optional;

public interface ReclamationServiceInterface {
    public Reclamation createReclamation(Reclamation reclamation,Long userId ) ;
    Reclamation updateReclamation(Long id, Reclamation updated);
    void deleteReclamation(Long id);
    List<Reclamation> getAllReclamations();
    Optional<Reclamation> getReclamationById(Long id);

    Reclamation assignReclamation(Long reclamationId, Long userId);

    // ✅ Méthodes ajoutées
    Reclamation respondToReclamation(Long reclamationId, String response);
    Reclamation rejectResponse(Long reclamationId);
    public Reclamation inProgressResponse(Long reclamationId, Long userId) ;

    }
