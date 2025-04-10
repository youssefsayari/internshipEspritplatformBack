package tn.esprit.innoxpert.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Skill;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TutorMatchingService {

    @Autowired
    private UserRepository userRepository;

    public Set<User> findMatchingTutors(User student) {
        List<Skill> studentSkills = student.getSkills();

        return userRepository.findByTypeUser(TypeUser.Tutor).stream()
                .filter(tutor -> hasMatchingExpertise(tutor, studentSkills))
                .sorted(Comparator.comparingInt(t -> -matchingScore(t, studentSkills))) // Sort descending
                .limit(3)
                .collect(Collectors.toSet());
    }

    private boolean hasMatchingExpertise(User tutor, List<Skill> studentSkills) {
        return tutor.getSkills().stream()
                .anyMatch(tutorSkill -> studentSkills.contains(tutorSkill));
    }

    private int matchingScore(User tutor, List<Skill> studentSkills) {
        return (int) tutor.getSkills().stream()
                .filter(studentSkills::contains)
                .count();
    }
}