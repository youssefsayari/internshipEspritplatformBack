package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.SchedulingConflictException;
import tn.esprit.innoxpert.Repository.DefenseRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class DefenseConflictChecker {

    private static final int DEFENSE_DURATION_MINUTES = 30;
    private static final int BUFFER_MINUTES = 30;
    private static final int TOTAL_SLOT_MINUTES = DEFENSE_DURATION_MINUTES + BUFFER_MINUTES;

    private final DefenseRepository defenseRepository;

    public void checkDefenseConflicts(String classroom, LocalDate date, LocalTime startTime, Set<User> tutors) {
        if (!isClassroomAvailable(classroom, date, startTime)) {
            throw new SchedulingConflictException("Classroom " + classroom + " is already booked at this time");
        }

        if (!areTutorsAvailable(tutors, date, startTime)) {
            throw new SchedulingConflictException("One or more tutors are not available at this time");
        }
    }

    public boolean isClassroomAvailable(String classroom, LocalDate date, LocalTime startTime) {
        LocalTime endTime = startTime.plusMinutes(TOTAL_SLOT_MINUTES);

        List<Defense> existingDefenses = defenseRepository.findByClassroomAndDefenseDate(classroom, date);

        return existingDefenses.stream().noneMatch(existing -> {
            LocalTime existingStart = existing.getDefenseTime();
            LocalTime existingEnd = existingStart.plusMinutes(TOTAL_SLOT_MINUTES);

            return !startTime.isAfter(existingEnd) && !endTime.isBefore(existingStart);
        });
    }

    private boolean areTutorsAvailable(Set<User> tutors, LocalDate date, LocalTime startTime) {
        LocalTime endTime = startTime.plusMinutes(TOTAL_SLOT_MINUTES);

        for (User tutor : tutors) {
            List<Defense> tutorDefenses = defenseRepository.findByTutorsContainingAndDefenseDate(tutor, date);

            boolean hasConflict = tutorDefenses.stream().anyMatch(defense -> {
                LocalTime defenseStart = defense.getDefenseTime();
                LocalTime defenseEnd = defenseStart.plusMinutes(TOTAL_SLOT_MINUTES);
                return !startTime.isAfter(defenseEnd) && !endTime.isBefore(defenseStart);
            });

            if (hasConflict) {
                return false;
            }
        }
        return true;
    }
}