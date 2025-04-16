package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.HistoryLog;
import tn.esprit.innoxpert.Repository.HistoryLogRepository;

import java.time.LocalDateTime;
import java.util.List;
@Service
@AllArgsConstructor
public class HistoryLogService implements HistoryLogServiceInterface {

    private final HistoryLogRepository historyLogRepository;

    @Override
    public void log(String actionType, String description) {
        HistoryLog log = HistoryLog.builder()
                .actionType(actionType)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();
        historyLogRepository.save(log);
    }

    @Override
    public List<HistoryLog> getAllLogs() {
        return historyLogRepository.findAll();
    }
}
