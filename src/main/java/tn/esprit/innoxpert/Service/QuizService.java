package tn.esprit.innoxpert.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.QuestionReponse;
import tn.esprit.innoxpert.Entity.Quiz;
import tn.esprit.innoxpert.Entity.Societe;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.QuizRepository;
import tn.esprit.innoxpert.Repository.SocieteRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional

public class QuizService implements QuizServiceInterface {
    @Autowired
    private QuizRepository quizRepository;
    private SocieteRepository societeRepository;
    @Override



    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        System.out.println("Quizzes récupérés : " + quizzes.size());
        quizzes.forEach(q -> System.out.println(q.getTitre()));
        return quizzes;
    }
    @Override

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }



   @Override
    public Quiz updateQuiz(Quiz quiz) {
        if ( !quizRepository.existsById(quiz.getIdQuiz())) {
            throw new NotFoundException("Quiz with ID: " + quiz.getIdQuiz() + " was not found. Cannot update.");
        }
        return quizRepository.save(quiz);
    }
    @Override
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
    @Override
    public Quiz addAndaffectQuizToSociete(Long idSociete, Quiz newQuiz) {
        Societe societe = societeRepository.findById(idSociete)
                .orElseThrow(() -> new NotFoundException("Quiz with ID: " + idSociete + " not found"));

        newQuiz.setSociete(societe);
        return quizRepository.save(newQuiz);    }

}
