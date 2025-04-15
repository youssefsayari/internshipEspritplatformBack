package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.TimeLineResponse;
import tn.esprit.innoxpert.Entity.Agreement;
import tn.esprit.innoxpert.Entity.TimeLine;
import tn.esprit.innoxpert.Entity.TypeAgreement;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Repository.AgreementRemarkRepository;
import tn.esprit.innoxpert.Repository.AgreementRepository;
import tn.esprit.innoxpert.Repository.TimeLineRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimeLineService implements TimeLineServiceInterface {
    @Autowired
    TimeLineRepository timeLineRepository;
    @Autowired
    AgreementRepository agreementRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void addTimeLine(Long userId, Long agreementId) {

        List<TimeLine> existingTimeLines = timeLineRepository.findByStudent_IdUser(userId);
        if (!existingTimeLines.isEmpty()) {
            throw new IllegalArgumentException("A timeline already exists for this student and agreement.");
        }

        Agreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new IllegalArgumentException("Agreement not found!"));
        User student = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));

        Date startDate = agreement.getStartDate();
        Date endDate = agreement.getEndDate();

        List<Date> timelineDates = calculateTimelineDates(startDate, endDate);

        List<String> titles = Arrays.asList(
                "Agreement Request",
                "Work Plan Submission",
                "Technical Validation",
                "Report Submission"
        );

        List<String> descriptions = Arrays.asList(
                "Details of the internship agreement.",
                "Fill in all the required details in the attached document after downloading it to your PC.",
                "Information regarding the technical validation.",
                "Upload your internship report."
        );
        Date validationTechniqueDate = timelineDates.get(2);
        validationTechniqueDate = adjustWeekendDate(validationTechniqueDate);

        timelineDates.set(2, validationTechniqueDate);

        List<TimeLine> timeLines = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TimeLine timeLine = new TimeLine();
            timeLine.setTitle(titles.get(i));
            timeLine.setDescription(descriptions.get(i));
            timeLine.setDateLimite(timelineDates.get(i));
            timeLine.setStudent(student);
            timeLine.setDocument(null);
            if ("Agreement Request".equals(titles.get(i))) {
                timeLine.setTimeLaneState(TypeAgreement.ACCEPTED);
            }
            timeLines.add(timeLine);
        }


        timeLineRepository.saveAll(timeLines);
    }

    private Date adjustWeekendDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        return calendar.getTime();
    }

    @Override
    public List<TimeLineResponse> getTimeLinesByUserId(Long userId) {
        List<TimeLine> timelines = timeLineRepository.findByStudent_IdUser(userId);
        return timelines.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    private TimeLineResponse mapToResponse(TimeLine timeline) {
        TimeLineResponse response = new TimeLineResponse();
        response.setId(timeline.getId());
        response.setTitle(timeline.getTitle());
        response.setDescription(timeline.getDescription());
        response.setDateLimite(timeline.getDateLimite().toString());
        response.setStudentId(timeline.getStudent() != null ? timeline.getStudent().getIdUser() : null);
        response.setDocumentId(timeline.getDocument() != null ? timeline.getDocument().getId() : null);
        response.setTimeLaneState(timeline.getTimeLaneState());
        return response;
    }


    private List<Date> calculateTimelineDates(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        long duration = endDate.getTime() - startDate.getTime();
        long interval = duration / 3;
        for (int i = 0; i <= 3; i++) {
            long currentDateTime = startDate.getTime() + (i * interval);
            dates.add(new Date(currentDateTime));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, -10);
        dates.add(calendar.getTime());
        return dates;
    }

    @Override
    public void acceptStep(String title, Long userId, Integer note) {
        TimeLine timeLine = timeLineRepository.findByTitleAndStudent_IdUser(title, userId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found for this user"));
        timeLine.setNote(note);
        timeLine.setTimeLaneState(TypeAgreement.ACCEPTED);
        timeLineRepository.save(timeLine);
    }

    @Override
    public void rejectStep(String title, Long userId, Integer note) {
        TimeLine timeLine = timeLineRepository.findByTitleAndStudent_IdUser(title, userId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found for this user"));
        timeLine.setNote(note);
        timeLine.setTimeLaneState(TypeAgreement.REJECTED);
        timeLineRepository.save(timeLine);
    }






}
