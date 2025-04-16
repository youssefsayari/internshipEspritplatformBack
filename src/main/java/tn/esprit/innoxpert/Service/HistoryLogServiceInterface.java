package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.HistoryLog;

import java.util.List;

public interface HistoryLogServiceInterface {

    // Ajouter un log
    void log(String actionType, String description);

    // (Optionnel) Récupérer tous les logs
    List<HistoryLog> getAllLogs();
}
