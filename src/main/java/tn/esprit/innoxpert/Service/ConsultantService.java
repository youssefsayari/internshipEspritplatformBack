package tn.esprit.innoxpert.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Consultant;
import tn.esprit.innoxpert.Entity.TimeSlot;
import tn.esprit.innoxpert.Repository.ConsultantRepository;
import tn.esprit.innoxpert.Repository.TimeSlotRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConsultantService {
    @Autowired
    private ConsultantRepository consultantRepository;
    private TimeSlotRepository timeSlotRepository;
    public Consultant saveConsultant(Consultant consultant) {
        return consultantRepository.save(consultant);
    }

    // Get all Consultants
    public List<Consultant> getAllConsultants() {
        return consultantRepository.findAll();
    }

    // Get Consultant by ID
    public Optional<Consultant> getConsultantById(Long id) {
        return consultantRepository.findById(id);
    }

    // Get Consultant by Email
    public Consultant getConsultantByEmail(String email) {
        return consultantRepository.findByEmail(email);
    }

    // Delete Consultant by ID
    public void deleteConsultant(Long id) {
        consultantRepository.deleteById(id);
    }

    public List<LocalDateTime> getAvailableSlotsForConsultant(Long consultantId) {
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(7); // Show availability for the next 7 days
        List<LocalDateTime> availableSlots = new ArrayList<>();

        // Fetch existing consultations (booked slots)
        List<TimeSlot> takenSlots = timeSlotRepository.findByConsultantAndStartTimeBetween(
                consultant,
                today.atStartOfDay(),
                endDate.atTime(LocalTime.MAX)
        );

        Set<LocalDateTime> takenStartTimes = takenSlots.stream()
                .map(TimeSlot::getStartTime)
                .collect(Collectors.toSet());

        Duration slotDuration = Duration.ofMinutes(40);
        LocalTime workStart = LocalTime.of(9, 0);
        LocalTime workEnd = LocalTime.of(17, 0);

        for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalTime time = workStart;
            while (!time.plus(slotDuration).isAfter(workEnd)) {
                LocalDateTime slotStart = LocalDateTime.of(date, time);
                if (!takenStartTimes.contains(slotStart)) {
                    availableSlots.add(slotStart);
                }
                time = time.plus(slotDuration);
            }
        }

        return availableSlots;
    }

}