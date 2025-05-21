package tn.esprit.innoxpert.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.*;

@Service
public class MistralAIService {

    @Value("${mistral.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.mistral.ai/v1/chat/completions";
    private final RestTemplate restTemplate;

    public MistralAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, String> generateEducationalQuestions(String internshipOffer) {
        try {
            String response = callAPI(buildRequest(internshipOffer));
            return parseResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("API call failed: " + e.getMessage());
        }
    }

    private String buildRequest(String content) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode requestNode = mapper.createObjectNode();

            String cleanContent = content.replaceAll("[^\\p{Print}]", "").trim();

            String prompt = """
                You are an AI that analyzes internship offers and extracts educational insights.
                Your goal is to generate exactly 5 questions and answers that help a student understand key technologies, frameworks, or concepts mentioned in the internship offer.

                Focus on what the intern is expected to learn or use.

                Respond ONLY in this format:
                Q: ... | A: ...
                Q: ... | A: ...
                Q: ... | A: ...
                Q: ... | A: ...
                Q: ... | A: ...

                If the offer has very few or no technical terms, return:
                NO_QA

                Internship Offer:
                """ + cleanContent;

            requestNode.put("model", "mistral-small-latest");
            requestNode.putArray("messages").addObject()
                    .put("role", "user")
                    .put("content", prompt);
            requestNode.put("temperature", 0.2);
            requestNode.put("max_tokens", 300);
            requestNode.put("top_p", 0.25);

            return mapper.writeValueAsString(requestNode);

        } catch (Exception e) {
            throw new RuntimeException("Failed to build request", e);
        }
    }

    private Map<String, String> parseResponse(String response) {
        Map<String, String> qna = new LinkedHashMap<>();
        try {
            JsonNode root = new ObjectMapper().readTree(response);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            System.out.println("API Raw Output: " + content);

            // Detect NO_QA
            if (content.trim().replaceAll("[^A-Za-z0-9]", "").equalsIgnoreCase("NOQA")) {
                return qna;
            }

            // Extract Q/A pairs
            Pattern pattern = Pattern.compile(
                    "Q:\\s*([^|]+?)\\s*\\|\\s*A:\\s*(.+?)(?=(\\s*Q:|\\s*$))",
                    Pattern.DOTALL | Pattern.CASE_INSENSITIVE
            );

            Matcher matcher = pattern.matcher(content);

            int count = 0;
            while (matcher.find() && count < 5) {
                String question = matcher.group(1).trim().replaceAll("[^\\p{Print}]", "").replaceAll("\\s+", " ");
                String answer = matcher.group(2).trim().replaceAll("[^\\p{Print}]", "").replaceAll("\\s+", " ");

                if (question.matches(".*[A-Za-z]{4,}.*") && answer.length() > 20) {
                    qna.put(question, answer);
                    count++;
                }
            }

            // If nothing valid and the text is unrelated → hard fail
            if (qna.isEmpty() && content.toLowerCase().contains("intern")) {
                throw new RuntimeException("Model returned unrelated or invalid content: " + content);
            }

        } catch (Exception e) {
            throw new RuntimeException("Parsing error: " + e.getMessage());
        }
        return qna;
    }

    private String callAPI(String requestBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("API Error: " + response.getBody());
            }

            return response.getBody();

        } catch (HttpClientErrorException e) {
            String errorDetails = String.format(
                    "Mistral API Error [%s]: %s",
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
            throw new RuntimeException(errorDetails);
        } catch (Exception e) {
            throw new RuntimeException("Network error: " + e.getMessage());
        }
    }
}
