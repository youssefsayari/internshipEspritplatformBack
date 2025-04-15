package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.innoxpert.Entity.TutorEvaluation;

import java.util.List;
import java.util.Optional;

public interface TutorEvaluationRepository extends JpaRepository<TutorEvaluation, Long> {

    // Use the exact property names from your entities
    List<TutorEvaluation> findByDefense_IdDefense(Long defenseId);

    Optional<TutorEvaluation> findByDefense_IdDefenseAndTutor_IdUser(Long defenseId, Long tutorId);

    boolean existsByDefense_IdDefenseAndTutor_IdUser(Long defenseId, Long tutorId);
}