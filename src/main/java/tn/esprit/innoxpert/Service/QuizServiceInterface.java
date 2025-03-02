package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Quiz;

import java.util.List;
import java.util.Optional;

public interface QuizServiceInterface {
    public List<Quiz> getAllQuizzes();

    public Optional<Quiz> getQuizById(Long id);


    public void deleteQuiz(Long id);

    public Quiz updateQuiz(Quiz quiz);
    public Quiz addAndaffectQuizToSociete(Long idSociete,Quiz newQuiz);


}