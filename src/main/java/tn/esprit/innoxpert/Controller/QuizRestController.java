package tn.esprit.innoxpert.Controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Quiz;
import tn.esprit.innoxpert.Service.QuizServiceInterface;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Quiz Management")
@RestController
@AllArgsConstructor
@RequestMapping("/quiz")
public class QuizRestController {


    private final QuizServiceInterface quizService;

    @JsonIgnore
    @GetMapping("/getAllQuizzes")
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/getQuizById/{idQuiz}")
    public Optional<Quiz> getQuizById(@PathVariable("idQuiz") Long idQuiz) {
        return quizService.getQuizById(idQuiz);
    }



    @PutMapping("/updateQuiz")

    public Quiz updateQuiz(@RequestBody Quiz quiz)
    {
        return quizService.updateQuiz(quiz);
    }

    @DeleteMapping("/deleteQuiz/{idQuiz}")
    public void deleteQuiz(@PathVariable("idQuiz") Long idQuiz) {
        quizService.deleteQuiz(idQuiz);
    }

    @PostMapping("/addQuizAndAssignToSociete/{idSociete}")
    public ResponseEntity<?> addQuizAndAssignToSociete(@Valid @RequestBody Quiz quiz,
                                                       @PathVariable("idSociete") Long idSociete,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(quizService.addAndaffectQuizToSociete(idSociete, quiz));
    }
}
