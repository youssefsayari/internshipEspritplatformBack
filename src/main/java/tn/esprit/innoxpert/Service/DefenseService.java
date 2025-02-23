package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.DefenseRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DefenseService implements DefenseServiceInterface {

    @Autowired
    private final DefenseRepository defenseRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public List<Defense> getAllDefenses() {
        return defenseRepository.findAll();
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
    public Defense addDefense(Defense d) {
        if (!d.isReportSubmitted() || !d.isInternshipCompleted()) {
            throw new IllegalArgumentException("Cannot add defense. Report must be submitted and internship must be completed.");
        }

        // Ensure the student exists and has the correct type
        User student = userRepository.findById(d.getStudent().getIdUser())
                .orElseThrow(() -> new NotFoundException("Student with ID: " + d.getStudent().getIdUser() + " was not found."));

        if (student.getTypeUser() != TypeUser.Student) {
            throw new IllegalArgumentException("Assigned user must be a student.");
        }

            // Ensure the student does not already have a defense
            if (defenseRepository.existsByStudent(student)) {
                throw new IllegalArgumentException("This student already has a defense.");
            }

        // Ensure exactly 3 tutors are assigned
        if (d.getTutors().size() != 3) {
            throw new IllegalArgumentException("A defense must have exactly 3 tutors.");
        }

        // Ensure all tutors are of type Tutor
        for (User tutor : d.getTutors()) {
            User foundTutor = userRepository.findById(tutor.getIdUser())
                    .orElseThrow(() -> new NotFoundException("Tutor with ID: " + tutor.getIdUser() + " was not found."));

            if (foundTutor.getTypeUser() != TypeUser.Tutor) {
                throw new IllegalArgumentException("Only users of type 'Tutor' can be assigned as tutors.");
            }
        }

        return defenseRepository.save(d);
    }

    @Override
    public void removeDefenseById(Long idDefense) {
        if (!defenseRepository.existsById(idDefense)) {
            throw new NotFoundException("User with ID :  " + idDefense + " was not found.");
        }
        defenseRepository.deleteById(idDefense);
    }

    @Override
    public Defense updateDefense(Defense d) {
        if (!defenseRepository.existsById(d.getIdDefense())) {
            throw new NotFoundException("Defense with ID: " + d.getIdDefense() + " was not found. Cannot update.");
        }
        return defenseRepository.save(d);
    }
}
