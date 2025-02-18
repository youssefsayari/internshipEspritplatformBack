package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Quiz;
import tn.esprit.innoxpert.Service.QuizServiceInterface;

import java.util.List;
import java.util.Optional;

@Tag(name = "Quiz Management")
@RestController
@AllArgsConstructor
@RequestMapping("/quiz")
public class QuizRestController {
    @Autowired
    QuizServiceInterface quizService;

    @GetMapping("/getAllQuizzes")
    public List<Quiz> getAllQuizzes()
    {
        return quizService.getAllQuizzes();
    }
    @GetMapping("/getQuizById/{idQuiz}")
    public Optional<Quiz> getQuizById(@PathVariable("idQuiz") Long idQuiz)
    {
        return quizService.getQuizById(idQuiz);
    }
    @PostMapping("/addQuiz")
    public Quiz createQuiz ( @RequestBody Quiz quiz)
    {
        return quizService.createQuiz(quiz);
    }

    @DeleteMapping("/deleteQuiz/{idQuiz}")
    public void deleteQuiz(@PathVariable ("idQuiz") Long idQuiz)
    {
        quizService.deleteQuiz(idQuiz);
    }

    @PutMapping("/updateQuiz")

    public Quiz updateQuiz(@RequestBody Quiz quiz)
    {
        return quizService.updateQuiz(quiz);
    }



    @PostMapping("/addQuizAndAssignToSociete/{idSociete}")
    public Quiz addQuizAndAssignToSociete(@RequestBody Quiz quiz, @PathVariable("idSociete") Long idSociete) {
        return quizService.addAndaffectQuizToSociete(idSociete, quiz);
    }






}
