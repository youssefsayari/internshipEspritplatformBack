    package tn.esprit.innoxpert.Util;

    import com.fasterxml.jackson.databind.JsonNode;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.http.*;
    import org.springframework.stereotype.Service;
    import org.springframework.web.client.HttpClientErrorException;
    import org.springframework.web.client.RestTemplate;
    import java.util.*;
    import java.util.regex.*;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.fasterxml.jackson.databind.node.ObjectNode;

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
                // Nettoyage unifié
                String cleanContent = content
                        .replaceAll("[^\\p{Print}]", "")
                        .trim();

                String prompt = String.format(
                        "Act as a Career Counselor for Computer Science students. Generate 5 educational Q/A pairs that:\n"
                                + "- Explain underlying technical concepts\n"
                                + "- Connect to academic courses\n"
                                + "- Reveal industry best practices\n"
                                + "- Provide career advice\n\n"
                                + "Format rules:\n"
                                + "1. Answers must teach new concepts (25-40 words)\n"
                                + "2. Include 1 theory question, 1 tool question, 1 best practice, 1 career tip, 1 trend\n"
                                + "3. Use examples beyond the internship description\n\n"
                                + "Examples:\n"
                                + "Q: Why learn distributed systems for cloud roles? | A: Distributed systems handle scaling through sharding and replication, crucial for managing cloud workloads (ACID vs BASE tradeoffs)\n"
                                + "Q: What's the value of CI/CD pipelines? | A: CI/CD automates testing/deployment, reducing human error while enabling rapid iterations (learn GitLab CI or GitHub Actions)\n\n"
                                + "Internship Offer:\n%s\n\n"
                                + "Generate exactly 5 educational Q/A pairs:",
                        cleanContent
                );

                requestNode.put("model", "mistral-small-latest");
                requestNode.putArray("messages").addObject()
                        .put("role", "user")
                        .put("content", prompt);
                requestNode.put("temperature", 0.85);
                requestNode.put("max_tokens", 600);
                requestNode.put("top_p", 0.9);

                return mapper.writeValueAsString(requestNode);

            } catch (Exception e) {
                throw new RuntimeException("Failed to build request", e);
            }
        }

        private String callAPI(String requestBody) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + apiKey);
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(
                        API_URL,
                        entity,
                        String.class
                );

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

        private Map<String, String> parseResponse(String response) {
            Map<String, String> qna = new LinkedHashMap<>();
            try {
                JsonNode root = new ObjectMapper().readTree(response);
                String content = root.path("choices").get(0).path("message").path("content").asText();

                Pattern pattern = Pattern.compile("Q: (.*?)\\s*\\|\\s*A: (.*?)(?=(\\s*Q:|$))");
                Matcher matcher = pattern.matcher(content);

                int count = 0;
                while (matcher.find() && count < 5) {
                    String question = matcher.group(1).trim()
                            .replaceAll("[^\\p{Print}]", "") // Remove non-printable chars
                            .replaceAll("\\s+", " ");

                    String answer = matcher.group(2).trim()
                            .replaceAll("[^\\p{Print}]", "")
                            .replaceAll("\\s+", " ");

                    if (!question.isEmpty() && isValidAnswer(answer)) {
                        qna.put(question, answer);
                        count++;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response: " + e.getMessage());
            }
            return qna;
        }

        private boolean isValidAnswer(String answer) {
            return answer.length() > 40 // Réduit à 40 caractères
                    && answer.split("\\s+").length > 8 // Réduit à 8 mots
                    && !answer.matches("(?i).*(intern will|you will|our company).*");
        }

    }