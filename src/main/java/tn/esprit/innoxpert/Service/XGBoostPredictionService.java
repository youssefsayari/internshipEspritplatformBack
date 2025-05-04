package tn.esprit.innoxpert.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class XGBoostPredictionService {

    public double predict(String option, String subject, String company) {
        String url = "http://localhost:8000/predict";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> body = new HashMap<>();
        body.put("option", option);
        body.put("subject", subject);
        body.put("company", company);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Object probObj = response.getBody().get("probability");
            return Double.parseDouble(probObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
