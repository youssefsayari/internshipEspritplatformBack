package tn.esprit.innoxpert.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.CompanyRepository;
import tn.esprit.innoxpert.Repository.QuizRepository;
import tn.esprit.innoxpert.Repository.SocieteRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional

public class QuizService implements QuizServiceInterface {
    @Autowired
    private QuizRepository quizRepository;
    private SocieteRepository societeRepository;
    private UserRepository userRepository;
    private CompanyRepository companyRepository;
    @Override



    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        System.out.println("Quizzes récupérés : " + quizzes.size());
        quizzes.forEach(q -> System.out.println(q.getTitre()));
        return quizzes;
    }
    @Override

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }



   @Override
    public Quiz updateQuiz(Quiz quiz) {
        Company s=quizRepository.findCompanyByQuizId(quiz.getIdQuiz());
        if ( !quizRepository.existsById(quiz.getIdQuiz())) {
            throw new NotFoundException("Quiz with ID: " + quiz.getIdQuiz() + " was not found. Cannot update.");
        }
       System.out.println("aaaa" +s.getId());
       System.out.println(quiz.getCompany());
       quiz.setCompany(s);
        return quizRepository.save(quiz);
    }
    @Override
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
    @Override
    public Quiz addAndaffectQuizToSociete(Long idSociete, Quiz newQuiz) {
        Company company = companyRepository.findByOwnerId(idSociete);

        newQuiz.setCompany(company);
        return quizRepository.save(newQuiz);    }

}
