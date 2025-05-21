package tn.esprit.innoxpert.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
    @Slf4j
    public class AiEmbeddingService {

        private final HttpClient httpClient = HttpClient.newHttpClient();
        private final ObjectMapper objectMapper = new ObjectMapper();

        public float[] getEmbedding(String text) throws Exception {
            String json = objectMapper.writeValueAsString(new RequestText(text));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:5000/get-embedding"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode node = objectMapper.readTree(response.body());
            JsonNode embeddingNode = node.get("embedding");

            float[] embedding = new float[embeddingNode.size()];
            for (int i = 0; i < embeddingNode.size(); i++) {
                embedding[i] = embeddingNode.get(i).floatValue();
            }

            return embedding;
        }

    public List<float[]> getEmbeddings(List<String> texts) throws Exception {
        String json = objectMapper.writeValueAsString(new RequestTexts(texts));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:5000/get-embeddings"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode node = objectMapper.readTree(response.body()).get("embeddings");

        List<float[]> embeddings = new ArrayList<>();
        for (JsonNode embeddingNode : node) {
            float[] embedding = new float[embeddingNode.size()];
            for (int i = 0; i < embeddingNode.size(); i++) {
                embedding[i] = embeddingNode.get(i).floatValue();
            }
            embeddings.add(embedding);
        }

        return embeddings;
    }

        private record RequestText(String text) {}
        private record RequestTexts(List<String> texts) {}
    }


