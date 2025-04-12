package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.DTO.TimeLineResponse;
import tn.esprit.innoxpert.Entity.TimeLine;

import java.util.List;

public interface TimeLineServiceInterface {
    void addTimeLine(Long userId, Long agreementId);
    List<TimeLineResponse> getTimeLinesByUserId(Long userId);
    void acceptStep(String title, Long userId, Integer note);
    void rejectStep(String title, Long userId, Integer note);

}
