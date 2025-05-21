package tn.esprit.innoxpert.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZoomTokenService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${zoom.account-id}")
    private String accountId;
    @Value("${zoom.client-id}")
    private String clientId;
    @Value("${zoom.client-secret}")
    private String clientSecret;
    @Value("${zoom.token-url}")
    private String tokenUrl;

    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "account_credentials");
        params.add("account_id", accountId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        return (String) response.getBody().get("access_token");
    }
}

