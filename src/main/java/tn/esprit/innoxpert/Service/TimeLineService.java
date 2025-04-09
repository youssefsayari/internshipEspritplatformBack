package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.TimeLineResponse;
import tn.esprit.innoxpert.Entity.Agreement;
import tn.esprit.innoxpert.Entity.TimeLine;
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
                "Depot Journal de bord",
                "Depot Bilan Version 1",
                "Lancement Visite Mi Parcours",
                "Validation Technique",
                "Depot Rapport Version 1",
                "Depot Rapport Final"
        );

        List<TimeLine> timeLines = new ArrayList<>();
        int z = 5;
        for (int i = 0; i < 6; i++) {
            TimeLine timeLine = new TimeLine();
            timeLine.setTitle(titles.get(i));
            timeLine.setDescription(titles.get(i));
            timeLine.setDateLimite(timelineDates.get(z));
            timeLine.setStudent(student);
            timeLine.setDocument(null);
            timeLines.add(timeLine);
            z--;
        }


        timeLineRepository.saveAll(timeLines);
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
        response.setDateLimite(timeline.getDateLimite().toString()); // Ou formater selon besoin
        response.setStudentId(timeline.getStudent() != null ? timeline.getStudent().getIdUser() : null);
        response.setDocumentId(timeline.getDocument() != null ? timeline.getDocument().getId() : null);
        return response;
    }


    private List<Date> calculateTimelineDates(Date startDate, Date endDate) {
        List<Date> dates = new ArrayList<>();
        long duration = endDate.getTime() - startDate.getTime();
        long interval = duration / 5;
        for (int i = 0; i <= 5; i++) {
            long currentDateTime = startDate.getTime() + (i * interval);
            dates.add(new Date(currentDateTime));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.DATE, -10);
        dates.add(calendar.getTime());
        return dates;
    }





}
