package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.QuizUser;

import tn.esprit.innoxpert.Repository.QuizUserRepository;
import tn.esprit.innoxpert.Service.QuizUserService;



@Tag(name = "Question Management")
@AllArgsConstructor
@RestController
@RequestMapping("/quiz-user")
public class QuizUserRestController {


    private QuizUserService quizUserService;
    private QuizUserRepository quizUserRepository;

    @PostMapping("/saveQuizResult")
    public QuizUser saveQuizResult (@RequestBody QuizUser quizUser)
    {
        return quizUserService.saveQuizResult(quizUser);
    }
    @GetMapping("/exists/{userId}/{quizId}")
    public ResponseEntity<Boolean> hasUserTakenQuiz(@PathVariable Long userId, @PathVariable Long quizId) {
        boolean exists = quizUserRepository.existsByIdUserAndIdQuiz(userId, quizId);
        return ResponseEntity.ok(exists);
    }

}
