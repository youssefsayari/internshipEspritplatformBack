package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Quiz;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {


}
