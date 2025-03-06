package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.QuizRepository;
import tn.esprit.innoxpert.Repository.QuizUserRepository;
import tn.esprit.innoxpert.Repository.SocieteRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class QuizUserService {

    private QuizUserRepository quizUserRepository;

    public QuizUser saveQuizResult(QuizUser quizUser) {
       return quizUserRepository.save(quizUser);
    }
}