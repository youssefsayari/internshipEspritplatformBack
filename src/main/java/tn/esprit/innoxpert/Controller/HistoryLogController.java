package tn.esprit.innoxpert.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.HistoryLog;
import tn.esprit.innoxpert.Repository.HistoryLogRepository;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@AllArgsConstructor
public class HistoryLogController {

    private final HistoryLogRepository historyLogRepository;

    @GetMapping
    public List<HistoryLog> getAllLogs() {
        return historyLogRepository.findAll();
    }
}
