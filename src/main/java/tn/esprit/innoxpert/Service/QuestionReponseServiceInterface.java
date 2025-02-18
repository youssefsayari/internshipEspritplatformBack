package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.QuestionReponse;

import java.util.List;
import java.util.Optional;

public interface QuestionReponseServiceInterface {
    List<QuestionReponse> getQuestionsByQuizId(Long quizId);
    Optional<QuestionReponse> getQuestionById(Long id);
    QuestionReponse createQuestion(QuestionReponse question);
    QuestionReponse updateQuestion(QuestionReponse question);
    void deleteQuestion(Long id);
    public QuestionReponse addAndaffectQuestionToQuiz(Long idQuiz, QuestionReponse newQuestion);

}
