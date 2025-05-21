package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.QuizUser;
import tn.esprit.innoxpert.Entity.Societe;

@Repository
public interface QuizUserRepository extends JpaRepository<QuizUser,Long> {
    boolean existsByIdUserAndIdQuiz(Long idUser, Long idQuiz);

}
