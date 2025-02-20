package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.QuestionReponse;
import tn.esprit.innoxpert.Service.QuestionReponseServiceInterface;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Question Management")
@RestController
@AllArgsConstructor
@RequestMapping("/question")
public class QuestionReponseRestController {

    private final QuestionReponseServiceInterface questionService;

    @GetMapping("/getQuestionsByQuizId/{idQuiz}")
    public List<QuestionReponse> getQuestionsByQuizId(@PathVariable("idQuiz") Long idQuiz) {
        return questionService.getQuestionsByQuizId(idQuiz);
    }

    @GetMapping("/getQuestionById/{idQuestionReponse}")
    public Optional<QuestionReponse> getQuestionById(@PathVariable("idQuestionReponse") Long idQuestionReponse) {
        return questionService.getQuestionById(idQuestionReponse);
    }


    @PutMapping("/updateQuestion")

    public QuestionReponse updateQuestion(@RequestBody QuestionReponse questionReponse)
    {
        return questionService.updateQuestion(questionReponse);
    }


    @DeleteMapping("/deleteQuestion/{idQuestionReponse}")
    public void deleteQuestion(@PathVariable("idQuestionReponse") Long idQuestionReponse) {
        questionService.deleteQuestion(idQuestionReponse);
    }

    @PostMapping("/addQuestionAndAssignToQuiz/{idQuiz}")
    public ResponseEntity<?> addQuestionAndAssignToQuiz(@Valid @RequestBody QuestionReponse question,
                                                        @PathVariable("idQuiz") Long idQuiz,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(questionService.addAndaffectQuestionToQuiz(idQuiz, question));
    }
}
