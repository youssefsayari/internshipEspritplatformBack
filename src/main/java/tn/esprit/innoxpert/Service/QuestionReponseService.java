package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.QuestionReponse;
import tn.esprit.innoxpert.Entity.Quiz;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.QuestionReponseRepository;
import tn.esprit.innoxpert.Repository.QuizRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuestionReponseService implements QuestionReponseServiceInterface {
    @Autowired
    private QuestionReponseRepository questionRepository;
    private QuizRepository quizRepository;

    public List<QuestionReponse> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuiz_IdQuiz(quizId);
    }

    public Optional<QuestionReponse> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public QuestionReponse createQuestion(QuestionReponse question) {
        return questionRepository.save(question);
    }


    public QuestionReponse updateQuestion(QuestionReponse questionReponse) {
        if ( !questionRepository.existsById(questionReponse.getIdQuestionReponse())) {
            throw new NotFoundException("Question with ID: " + questionReponse.getIdQuestionReponse() + " was not found. Cannot update.");
        }
        return questionRepository.save(questionReponse);
    }
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public QuestionReponse addAndaffectQuestionToQuiz(Long idQuiz, QuestionReponse newQuestion) {
        Quiz quiz = quizRepository.findById(idQuiz)
                .orElseThrow(() -> new NotFoundException("Quiz with ID: " + idQuiz + " not found"));

        newQuestion.setQuiz(quiz);
        return questionRepository.save(newQuestion);    }
}