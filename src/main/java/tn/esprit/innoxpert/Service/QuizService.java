package tn.esprit.innoxpert.Service;

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
public class QuizService implements QuizServiceInterface {
    @Autowired
    private QuizRepository quizRepository;
    private SocieteRepository societeRepository;


    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }


    public Quiz updateQuiz(Quiz quiz) {
        if ( !quizRepository.existsById(quiz.getIdQuiz())) {
            throw new NotFoundException("Quiz with ID: " + quiz.getIdQuiz() + " was not found. Cannot update.");
        }
        return quizRepository.save(quiz);
    }
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
    public Quiz addAndaffectQuizToSociete(Long idSociete, Quiz newQuiz) {
        Societe societe = societeRepository.findById(idSociete)
                .orElseThrow(() -> new NotFoundException("Quiz with ID: " + idSociete + " not found"));

        newQuiz.setSociete(societe);
        return quizRepository.save(newQuiz);    }

}
