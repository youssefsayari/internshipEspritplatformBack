package tn.esprit.innoxpert.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.DefenseRequest;
import tn.esprit.innoxpert.DTO.DefenseWithEvaluationsDTO;
import tn.esprit.innoxpert.DTO.TutorEvaluationDTO;
import tn.esprit.innoxpert.DTO.UserDTO;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.DefenseRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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
        // 1. Validate the student
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found"));

        if (student.getTypeUser() != TypeUser.Student) {
            throw new IllegalArgumentException("User with ID " + studentId + " is not a student");
        }

        // 2. Check if the student already has a defense
        Optional<Defense> existingDefense = defenseRepository.findDefenseByStudentId(studentId);
        if (existingDefense.isPresent()) {
            throw new IllegalArgumentException("This student already has a scheduled defense.");
        }

        // 3. Validate the tutors (exactly 3 tutors)
        Set<Long> tutorIds = defenseRequest.getTutorIds();
        if (tutorIds == null || tutorIds.size() != 3) {
            throw new IllegalArgumentException("Exactly 3 tutor IDs must be specified");
        }

        List<User> tutors = userRepository.findAllById(tutorIds);
        if (tutors.size() != 3) {
            throw new IllegalArgumentException("One or more tutors not found");
        }

        for (User tutor : tutors) {
            if (tutor.getTypeUser() != TypeUser.Tutor) {
                throw new IllegalArgumentException("User with ID " + tutor.getIdUser() + " is not a tutor");
            }
        }

        // 4. Check for scheduling conflicts
        conflictChecker.checkDefenseConflicts(
                defenseRequest.getClassroom(),
                defenseRequest.getDefenseDate(),
                defenseRequest.getDefenseTime(),
                new HashSet<>(tutors)
        );

        // 5. Validate date and time constraints
        if (defenseRequest.getDefenseDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Defense date must be today or in the future.");
        }

        if (defenseRequest.getDefenseTime().isBefore(LocalTime.parse("08:00")) ||
                defenseRequest.getDefenseTime().isAfter(LocalTime.parse("18:00"))) {
            throw new IllegalArgumentException("Defense time must be between 08:00 and 18:00.");
        }

        // 6. Create and save the defense
        Defense defense = new Defense();
        defense.setStudent(student);
        defense.setDefenseDate(defenseRequest.getDefenseDate());
        defense.setDefenseTime(defenseRequest.getDefenseTime());
        defense.setClassroom(defenseRequest.getClassroom());
        defense.setReportSubmitted(defenseRequest.isReportSubmitted());
        defense.setInternshipCompleted(defenseRequest.isInternshipCompleted());
        defense.setTutors(new HashSet<>(tutors));

        // 7. Check if defense degree is null and set it to 0
        if (defense.getDefenseDegree() == null) {
            defense.setDefenseDegree(0.0); // Set to 0 if null
        }

        // Save and return the defense
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
    @Transactional
    public Defense updateDefense(Long defenseId, DefenseRequest request) {
        Defense defense = defenseRepository.findById(defenseId)
                .orElseThrow(() -> new NotFoundException("Defense with ID: " + defenseId + " was not found."));

        // Prevent update if degree is not 0.0 (i.e., already evaluated)
        if (defense.getDefenseDegree() != 0.0) {
            throw new IllegalStateException("This defense has already been evaluated and cannot be updated.");
        }


        // Validate date
        if (defense.getDefenseDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Defense date must be today or in the future.");
        }

        // Validate time
        if (defense.getDefenseTime().isBefore(LocalTime.parse("08:00")) ||
                defense.getDefenseTime().isAfter(LocalTime.parse("18:00"))) {
            throw new IllegalArgumentException("Defense time must be between 08:00 and 18:00.");
        }

        // Apply new values
        defense.setDefenseDate(request.getDefenseDate());
        defense.setDefenseTime(request.getDefenseTime());
        defense.setClassroom(request.getClassroom());

        // Preserve original data
        defense.setTutors(defense.getTutors());
        defense.setStudent(defense.getStudent());
        defense.setDefenseDegree(defense.getDefenseDegree());

        return defenseRepository.save(defense);
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

    public List<DefenseWithEvaluationsDTO> getDefensesWithEvaluationsByTutor(Long tutorId) {
        List<Defense> defenses = defenseRepository.findDefensesByTutorId(tutorId);

        return defenses.stream().map(defense -> {
            DefenseWithEvaluationsDTO dto = new DefenseWithEvaluationsDTO();
            dto.setIdDefense(defense.getIdDefense());
            dto.setClassroom(defense.getClassroom());
            dto.setDefenseDate(defense.getDefenseDate());
            dto.setDefenseTime(defense.getDefenseTime());
            dto.setReportSubmitted(defense.isReportSubmitted());
            dto.setInternshipCompleted(defense.isInternshipCompleted());
            dto.setDefenseDegree(defense.getDefenseDegree());

            // Convert student
            User student = defense.getStudent();
            UserDTO studentDTO = new UserDTO();
            studentDTO.setIdUser(student.getIdUser());
            studentDTO.setFirstName(student.getFirstName());
            studentDTO.setLastName(student.getLastName());
            dto.setStudent(studentDTO);

            List<Long> tutorIds = defense.getTutors().stream()
                    .map(User::getIdUser)
                    .collect(Collectors.toList());
            dto.setTutors(tutorIds);

            // Convert evaluations
            List<TutorEvaluationDTO> evalDTOs = defense.getEvaluations().stream().map(eval -> {
                TutorEvaluationDTO evalDTO = new TutorEvaluationDTO();
                evalDTO.setId(eval.getId());
                evalDTO.setTutorId(eval.getTutor().getIdUser());
                evalDTO.setGrade(eval.getGrade());
                evalDTO.setRemarks(eval.getRemarks());
                evalDTO.setStatus(eval.getStatus().name());
                return evalDTO;
            }).collect(Collectors.toList());

            dto.setEvaluations(evalDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Defense> getDefensesByStudentId(Long studentId) {
        return defenseRepository.findDefensesByStudentId(studentId);
    }

    public Map<String, List<Defense>> getDefenseStats(List<Defense> defenses) {
        // Initialize the categories
        List<Defense> excellentStudents = new ArrayList<>();
        List<Defense> averageStudents = new ArrayList<>();
        List<Defense> badStudents = new ArrayList<>();
        List<Defense> notEvaluated = new ArrayList<>();

        // Classify each defense based on the defense degree
        for (Defense defense : defenses) {
            Double defenseDegree = defense.getDefenseDegree(); // Assuming it's a Double, which can be null

            if (defenseDegree == null || defenseDegree == 0.0) {
                notEvaluated.add(defense); // Add to "Not Evaluated" if defenseDegree is null or 0
            } else if (defenseDegree >= 15) {
                excellentStudents.add(defense);
            } else if (defenseDegree >= 10) {
                averageStudents.add(defense);
            } else if (defenseDegree > 0) { // Degree > 0 is bad but not null or 0
                badStudents.add(defense);
            }
        }

        // Prepare the result map with categorized students
        Map<String, List<Defense>> result = new HashMap<>();
        result.put("Excellent", excellentStudents);
        result.put("Average", averageStudents);
        result.put("Bad", badStudents);
        result.put("Not Evaluated", notEvaluated); // Add "Not Evaluated" category

        return result;
    }






}