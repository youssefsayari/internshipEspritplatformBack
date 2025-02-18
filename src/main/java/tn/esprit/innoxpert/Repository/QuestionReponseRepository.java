package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.QuestionReponse;

import java.util.List;


@Repository
public interface QuestionReponseRepository extends JpaRepository<QuestionReponse,Long> {

    List<QuestionReponse> findByQuiz_IdQuiz(Long idQuiz);
}
