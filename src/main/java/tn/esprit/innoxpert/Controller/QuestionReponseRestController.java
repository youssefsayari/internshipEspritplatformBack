package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.QuestionReponse;
import tn.esprit.innoxpert.Entity.Quiz;
import tn.esprit.innoxpert.Service.QuestionReponseServiceInterface;
import tn.esprit.innoxpert.Service.QuizServiceInterface;

import java.util.List;
import java.util.Optional;

@Tag(name = "Question Management")
@RestController
@AllArgsConstructor
@RequestMapping("/question")
public class QuestionReponseRestController {
    @Autowired
    QuestionReponseServiceInterface questionService;

    @GetMapping("/getQuestionsByQuizId/{idQuiz}")
    public List<QuestionReponse> getQuestionsByQuizId(@PathVariable("idQuiz")Long idQuiz)
    {
        return questionService.getQuestionsByQuizId(idQuiz);
    }
    @GetMapping("/getQuestionById/{idQuestionReponse}")
    public Optional<QuestionReponse> getQuestionById(@PathVariable("idQuestionReponse") Long idQuestionReponse)
    {
        return questionService.getQuestionById(idQuestionReponse);
    }
    @PostMapping("/addQuestion")
    public QuestionReponse createQuestion ( @RequestBody QuestionReponse question)
    {
        return questionService.createQuestion(question);
    }

    @DeleteMapping("/deleteQuestion/{idQuestionReponse}")
    public void deleteQuestion(@PathVariable ("idQuestionReponse") Long idQuestionReponse)
    {
        questionService.deleteQuestion(idQuestionReponse);
    }

    @PutMapping("/updateQuestion")

    public QuestionReponse updateQuestion(@RequestBody QuestionReponse questionReponse)
    {
        return questionService.updateQuestion(questionReponse);
    }



    @PostMapping("/addQuestionAndAssignToQuiz/{idQuiz}")
    public QuestionReponse addQuestionAndAssignToQuiz(@RequestBody QuestionReponse question, @PathVariable("idQuiz") Long idQuiz) {
        return questionService.addAndaffectQuestionToQuiz(idQuiz, question);
    }






}
