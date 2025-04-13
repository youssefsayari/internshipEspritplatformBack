package tn.esprit.innoxpert.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.EvaluationRequest;
import tn.esprit.innoxpert.DTO.EvaluationResponse;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.DefenseRepository;
import tn.esprit.innoxpert.Repository.TutorEvaluationRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EvaluationService {

    private final TutorEvaluationRepository evaluationRepository;
    private final DefenseRepository defenseRepository;
    private final UserRepository userRepository;

    @Transactional
    public EvaluationResponse submitEvaluation(EvaluationRequest request) {
        Defense defense = defenseRepository.findById(request.getDefenseId())
                .orElseThrow(() -> new NotFoundException("Defense not found"));

        User tutor = userRepository.findById(request.getTutorId())
                .orElseThrow(() -> new NotFoundException("Tutor not found"));

        if (tutor.getTypeUser() != TypeUser.Tutor) {
            throw new IllegalArgumentException("User is not a tutor");
        }

        if (request.getGrade() == null || request.getGrade() < 0 || request.getGrade() > 20) {
            throw new IllegalArgumentException("Grade must be between 0 and 20");
        }

        if (!defense.getTutors().contains(tutor)) {
            throw new IllegalArgumentException("Tutor is not assigned to this defense");
        }

        TutorEvaluation evaluation = evaluationRepository
                .findByDefense_IdDefenseAndTutor_IdUser(request.getDefenseId(), request.getTutorId())
                .orElse(null);

        if (evaluation == null) {
            evaluation = new TutorEvaluation();
            evaluation.setDefense(defense);
            evaluation.setTutor(tutor);
        }

        evaluation.setGrade(request.getGrade());
        evaluation.setRemarks(request.getRemarks());
        evaluation.setStatus(EvaluationStatus.SUBMITTED);

        TutorEvaluation savedEvaluation = evaluationRepository.save(evaluation);

        updateDefenseDegree(defense);

        return mapToResponse(savedEvaluation);
    }


    private void updateDefenseDegree(Defense defense) {
        // UPDATED METHOD CALL
        List<TutorEvaluation> evaluations = evaluationRepository.findByDefense_IdDefense(defense.getIdDefense());

        // Only calculate if all 3 evaluations are submitted
        if (evaluations.size() == 3 && evaluations.stream().allMatch(e -> e.getStatus() == EvaluationStatus.SUBMITTED)) {
            double average = evaluations.stream()
                    .mapToDouble(TutorEvaluation::getGrade)
                    .average()
                    .orElse(0.0);

            // Round to two decimal places
            BigDecimal roundedAverage = new BigDecimal(average).setScale(2, RoundingMode.HALF_UP);

            // Set the defense degree based on the rounded average
            defense.setDefenseDegree(roundedAverage.doubleValue()); // No need to multiply by 5
            defenseRepository.save(defense);
        }
    }

    public List<EvaluationResponse> getEvaluationsForDefense(Long defenseId) {
        // UPDATED METHOD CALL
        return evaluationRepository.findByDefense_IdDefense(defenseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EvaluationResponse getEvaluation(Long evaluationId) {
        return evaluationRepository.findById(evaluationId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Evaluation not found"));
    }

    private EvaluationResponse mapToResponse(TutorEvaluation evaluation) {
        EvaluationResponse response = new EvaluationResponse();
        response.setId(evaluation.getId());
        response.setDefenseId(evaluation.getDefense().getIdDefense());
        response.setTutorId(evaluation.getTutor().getIdUser());
        response.setGrade(evaluation.getGrade());
        response.setRemarks(evaluation.getRemarks());
        response.setStatus(evaluation.getStatus());
        return response;

    }
    public EvaluationResponse getEvaluationByDefenseAndTutor(Long defenseId, Long tutorId) {
        return evaluationRepository.findByDefense_IdDefenseAndTutor_IdUser(defenseId, tutorId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new NotFoundException("Evaluation not found"));
    }



}