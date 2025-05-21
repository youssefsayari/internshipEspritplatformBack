package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.Quiz;
import tn.esprit.innoxpert.Entity.Societe;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {
    @Query("SELECT q.company FROM Quiz q WHERE q.idQuiz = :idQuiz")
    Company findCompanyByQuizId(@Param("idQuiz") Long idQuiz);

}
