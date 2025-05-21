package tn.esprit.innoxpert.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final ZoomTokenService zoomTokenService;

    @Value("${zoom.api-base-url}")
    private String zoomApiUrl;



    public String createScheduledZoomMeeting(String topic, LocalDateTime startTime, LocalDateTime endTime) {
        String token = zoomTokenService.getAccessToken();
        String defaultHostEmail = "chamseddine.boughanmi@esprit.tn ";
        String url = zoomApiUrl + "/users/" + defaultHostEmail + "/meetings";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        long durationInMinutes = Duration.between(startTime, endTime).toMinutes();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        Map<String, Object> body = new HashMap<>();
        body.put("topic", topic);
        body.put("type", 2);
        body.put("start_time", startTime.format(formatter));
        body.put("duration", durationInMinutes);
        body.put("timezone", "Europe/Paris");

        Map<String, Object> settings = new HashMap<>();
        settings.put("join_before_host", true);
        settings.put("approval_type", 0);
        settings.put("host_video", true);
        settings.put("participant_video", true);

        body.put("settings", settings);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return (String) response.getBody().get("join_url");
        } else {
            throw new RuntimeException("Failed to create Zoom meeting: " + response.getStatusCode());
        }
    }

}