package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
}
