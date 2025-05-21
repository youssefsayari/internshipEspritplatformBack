package tn.esprit.innoxpert.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.TimeSlotDTO;
import tn.esprit.innoxpert.Entity.Consultant;
import tn.esprit.innoxpert.Entity.Consultation;
import tn.esprit.innoxpert.Entity.TimeSlot;
import tn.esprit.innoxpert.Repository.ConsultantRepository;
import tn.esprit.innoxpert.Repository.ConsultationRepository;
import tn.esprit.innoxpert.Repository.TimeSlotRepository;
import tn.esprit.innoxpert.Service.ConsultantService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/consultants")
public class ConsultantController {

    @Autowired
    private ConsultantService consultantService;
    @Autowired
    private ConsultantRepository consultantRepository;
    @Autowired
    private ConsultationRepository consultationRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    // Create or Update Consultant
    @PostMapping
    public ResponseEntity<?> createOrUpdateConsultant(@RequestBody Consultant consultant) {
        try{
            Consultant savedConsultant = consultantService.saveConsultant(consultant);
            return ResponseEntity.ok(savedConsultant);
    } catch (
    DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
    }

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateConsultant(@PathVariable Long id, @RequestBody Consultant updatedConsultant) {
        return consultantRepository.findById(id)
                .map(consultant -> {
                    consultant.setFullName(updatedConsultant.getFullName());
                    consultant.setEmail(updatedConsultant.getEmail());
                    consultant.setSpecialty(updatedConsultant.getSpecialty());
                    consultant.setProfileDescription(updatedConsultant.getProfileDescription());

                    try {
                        consultantRepository.save(consultant);
                        return ResponseEntity.ok(consultant);
                    } catch (DataIntegrityViolationException e) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/email-exists")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email,
                                                    @RequestParam(required = false) Long excludeId) {
        boolean exists;

        if (excludeId != null) {
            exists = consultantRepository.existsByEmailAndIdNot(email, excludeId);
        } else {
            exists = consultantRepository.existsByEmail(email);
        }

        return ResponseEntity.ok(exists);
    }
    // Get all Consultants
    @GetMapping
    public ResponseEntity<List<Consultant>> getAllConsultants() {
        List<Consultant> consultants = consultantService.getAllConsultants();
        return new ResponseEntity<>(consultants, HttpStatus.OK);
    }

    // Get Consultant by ID
    @GetMapping("/{id}")
    public ResponseEntity<Consultant> getConsultantById(@PathVariable Long id) {
        Optional<Consultant> consultant = consultantService.getConsultantById(id);
        if (consultant.isPresent()) {
            return new ResponseEntity<>(consultant.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get Consultant by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<Consultant> getConsultantByEmail(@PathVariable String email) {
        Consultant consultant = consultantService.getConsultantByEmail(email);
        if (consultant != null) {
            return new ResponseEntity<>(consultant, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete Consultant by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        consultantService.deleteConsultant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/consultants/{consultantId}/available-timeslots")
    public List<TimeSlot> getAvailableTimeSlots(
            @PathVariable Long consultantId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        List<TimeSlot> allSlots = timeSlotRepository.findByConsultantAndStartTimeBetween(consultant, from, to);
        return allSlots.stream()
                .filter(TimeSlot::isAvailable)
                .collect(Collectors.toList());
    }
    @GetMapping("/{consultantId}/timeslots")
    public List<TimeSlotDTO> getDynamicTimeSlots(
            @PathVariable Long consultantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        // Define working hours
        LocalDateTime startOfDay = date.atTime(9, 0);
        LocalDateTime endOfDay = date.atTime(17, 0);
        Duration slotLength = Duration.ofMinutes(30);

        List<Consultation> bookedConsultations = consultationRepository
                .findByConsultantAndSlotStartTimeBetween(consultant, startOfDay, endOfDay);

        Set<LocalDateTime> bookedStartTimes = bookedConsultations.stream()
                .map(c -> c.getSlot().getStartTime())
                .collect(Collectors.toSet());

        List<TimeSlotDTO> generatedSlots = new ArrayList<>();
        for (LocalDateTime current = startOfDay; current.isBefore(endOfDay); current = current.plus(slotLength)) {
            LocalDateTime end = current.plus(slotLength);
            boolean isAvailable = !bookedStartTimes.contains(current);
            generatedSlots.add(new TimeSlotDTO(current, end, isAvailable));
        }

        return generatedSlots;
    }
}
