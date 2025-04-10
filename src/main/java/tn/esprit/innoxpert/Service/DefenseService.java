package tn.esprit.innoxpert.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.DefenseRequest;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.DefenseRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class DefenseService implements DefenseServiceInterface {

    private final DefenseRepository defenseRepository;
    private final UserRepository userRepository;
    private final DefenseConflictChecker conflictChecker;

    @Override
    public List<Defense> getAllDefenses() {
        return defenseRepository.findAllWithStudentsAndTutors();
    }

    @Override
    public Defense getDfenseById(Long idDefense) {
        return defenseRepository.findById(idDefense)
                .orElseThrow(() -> new NotFoundException("Defense with ID: " + idDefense + " was not found."));
    }

    @Override
    public Defense addDefense(Long studentId, DefenseRequest defenseRequest) {
        // 1. First check if student exists in DB
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found"));

        // 2. Verify it's actually a student
        if (student.getTypeUser() != TypeUser.Student) {
            throw new IllegalArgumentException("User with ID " + studentId + " is not a student");
        }

        // 3. Validate and set tutors
        Set<Long> tutorIds = defenseRequest.getTutorIds();
        if (tutorIds == null || tutorIds.size() != 3) {
            throw new IllegalArgumentException("Exactly 3 tutor IDs must be specified");
        }

        List<User> tutors = userRepository.findAllById(tutorIds);
        if (tutors.size() != 3) {
            throw new IllegalArgumentException("One or more tutors not found");
        }

        // Verify all are tutors
        for (User tutor : tutors) {
            if (tutor.getTypeUser() != TypeUser.Tutor) {
                throw new IllegalArgumentException("User with ID " + tutor.getIdUser() + " is not a tutor");
            }
        }

        // Check for scheduling conflicts
        conflictChecker.checkDefenseConflicts(
                defenseRequest.getClassroom(),
                defenseRequest.getDefenseDate(),
                defenseRequest.getDefenseTime(),
                new HashSet<>(tutors)
        );

        // 4. Create new Defense object
        Defense defense = new Defense();
        defense.setStudent(student);
        defense.setDefenseDate(defenseRequest.getDefenseDate());
        defense.setDefenseTime(defenseRequest.getDefenseTime());
        defense.setClassroom(defenseRequest.getClassroom());
        defense.setReportSubmitted(defenseRequest.isReportSubmitted());
        defense.setInternshipCompleted(defenseRequest.isInternshipCompleted());
        defense.setDefenseDegree(defenseRequest.getDefenseDegree());
        defense.setTutors(new HashSet<>(tutors));

        // 5. Save the defense
        return defenseRepository.save(defense);
    }

    @Override
    @Transactional
    public void removeDefenseById(Long idDefense) {
        Defense defense = defenseRepository.findById(idDefense)
                .orElseThrow(() -> new NotFoundException("Defense with ID: " + idDefense + " was not found."));

        for (User tutor : defense.getTutors()) {
            tutor.getDefenses().remove(defense);
        }

        defense.getTutors().clear();
        defenseRepository.save(defense);
        defenseRepository.delete(defense);
    }

    @Override
    public Defense updateDefense(Defense d) {
        Defense existing = defenseRepository.findById(d.getIdDefense())
                .orElseThrow(() -> new NotFoundException("Defense with ID: " + d.getIdDefense() + " was not found."));

        // Skip conflict check if date/time/classroom didn't change
        if (!existing.getDefenseDate().equals(d.getDefenseDate()) ||
                !existing.getDefenseTime().equals(d.getDefenseTime()) ||
                !existing.getClassroom().equals(d.getClassroom())) {

            conflictChecker.checkDefenseConflicts(
                    d.getClassroom(),
                    d.getDefenseDate(),
                    d.getDefenseTime(),
                    d.getTutors()
            );
        }

        return defenseRepository.save(d);
    }

    @Override
    public boolean isDefenseSlotAvailable(String classroom, LocalDate date, LocalTime time) {
        return conflictChecker.isClassroomAvailable(classroom, date, time);
    }
    @Override
    public List<Defense> getDefensesByTutorId(Long tutorId) {
        // First verify the tutor exists
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new NotFoundException("Tutor with ID: " + tutorId + " was not found."));

        // Verify it's actually a tutor
        if (tutor.getTypeUser() != TypeUser.Tutor) {
            throw new IllegalArgumentException("User with ID " + tutorId + " is not a tutor");
        }

        return defenseRepository.findDefensesByTutorId(tutorId);
    }
}