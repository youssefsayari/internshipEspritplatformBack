package tn.esprit.innoxpert.Service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class XGBoostClassificationService {

    public String classify(String secteur, int annee, int employes, int estTech, double dynamisme) {
        String url = "http://localhost:8000/classify";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("secteur", secteur);
        body.put("annee", annee);
        body.put("employes", employes);
        body.put("est_tech", estTech);
        body.put("dynamisme", dynamisme);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            Object result = response.getBody().get("classification");
            return result.toString();  // "Startup" ou "Non-Startup"
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur";
        }
    }
}
