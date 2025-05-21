package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.QuestionReponse;
import tn.esprit.innoxpert.Entity.Quiz;
import tn.esprit.innoxpert.Entity.Societe;

import java.util.List;


@Repository
public interface QuestionReponseRepository extends JpaRepository<QuestionReponse,Long> {

    List<QuestionReponse> findByQuiz_IdQuiz(Long idQuiz);
    @Query("SELECT q.quiz FROM QuestionReponse q WHERE q.idQuestionReponse = :idQuestionReponse")
    Quiz findQuizByQuestionReponseId(@Param("idQuestionReponse") Long idQuestionReponse);


}
