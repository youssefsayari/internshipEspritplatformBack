package tn.esprit.innoxpert.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.DefenseRequest;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.DefenseRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefenseService implements DefenseServiceInterface {

    @Autowired
    private final DefenseRepository defenseRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public List<Defense> getAllDefenses() {
        return defenseRepository.findAllWithStudentsAndTutors();
    }

    @Override
    public Defense getDfenseById(Long idDefense) {
        return defenseRepository.findById(idDefense)
                .orElseThrow(() -> new NotFoundException("Defense with ID: " + idDefense + " was not found."));
    }

    /*private DefenseDTO convertToDefenseDTO(Defense defense) {
        DefenseDTO dto = new DefenseDTO();
        dto.setIdDefense(defense.getIdDefense());
        dto.setDefenseDate(defense.getDefenseDate());
        dto.setDefenseTime(defense.getDefenseTime());
        dto.setClassroom(defense.getClassroom());
        dto.setReportSubmitted(defense.isReportSubmitted());
        dto.setInternshipCompleted(defense.isInternshipCompleted());
        dto.setDefenseDegree(defense.getDefenseDegree());

        // Convert the student to a UserDTO
        dto.setStudent(new UserDTO(defense.getStudent()));

        // Convert the tutors to UserDTOs
        List<UserDTO> tutors = defense.getTutors().stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
        dto.setTutors(tutors);

        return dto;
    }*/


    @Override
    public Defense addDefense(Long studentId, DefenseRequest defenseRequest) {
        // 1. First check if student exists in DB
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found"));

        // 2. Verify it's actually a student
        if (student.getTypeUser() != TypeUser.Student) {
            throw new IllegalArgumentException("User with ID " + studentId + " is not a student");
        }

        // 3. Create new Defense object
        Defense defense = new Defense();
        defense.setStudent(student);
        defense.setDefenseDate(defenseRequest.getDefenseDate());
        defense.setDefenseTime(defenseRequest.getDefenseTime());
        defense.setClassroom(defenseRequest.getClassroom());
        defense.setReportSubmitted(defenseRequest.isReportSubmitted());
        defense.setInternshipCompleted(defenseRequest.isInternshipCompleted());
        defense.setDefenseDegree(defenseRequest.getDefenseDegree());

        // 4. Validate and set tutors
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

        defense.setTutors(new HashSet<>(tutors));

        // 5. Save the defense
        return defenseRepository.save(defense);
    }

    @Override
    @Transactional
    public void removeDefenseById(Long idDefense) {
        // Step 1: Fetch the defense entity
        Defense defense = defenseRepository.findById(idDefense)
                .orElseThrow(() -> new NotFoundException("Defense with ID: " + idDefense + " was not found."));

        // Step 2: Remove the defense-tutor relationship from the tutors' side
        for (User tutor : defense.getTutors()) {
            tutor.getDefenses().remove(defense);  // Remove the relationship from the tutor's side
        }

        // Step 3: Clear the tutors collection in the defense
        defense.getTutors().clear();  // Clear the tutors collection in the defense

        // Step 4: Ensure the changes to the join table are persisted
        defenseRepository.save(defense);  // This will update the join table

        // Step 5: Delete the defense entity itself
        defenseRepository.delete(defense);  // Delete the defense entity
    }

    @Override
    public Defense updateDefense(Defense d) {
        if (!defenseRepository.existsById(d.getIdDefense())) {
            throw new NotFoundException("Defense with ID: " + d.getIdDefense() + " was not found. Cannot update.");
        }
        return defenseRepository.save(d);
    }


}