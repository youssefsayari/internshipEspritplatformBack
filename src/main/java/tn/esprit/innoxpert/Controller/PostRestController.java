package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.PostAdminResponse;
import tn.esprit.innoxpert.Entity.Post;
import tn.esprit.innoxpert.Service.PostServiceInterface;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.ObjectError;
import org.springframework.http.CacheControl;
import tn.esprit.innoxpert.Util.MistralAIService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

@Tag(name = "Post Management")
@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostRestController {

    @Autowired
    PostServiceInterface postService;

    // GHASSEN
    @GetMapping("/getAllPostsDTO")
    public List<PostAdminResponse> getAllPostsDTO() {
        return postService.getAllPostsDTO();
    }

    @GetMapping("/getAllPosts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // Récupérer un post par ID
    @GetMapping("/getPostById/{postId}")
    public Post getPostById(@PathVariable("postId") Long postId) {
        return postService.getPostById(postId);
    }

    // Récupérer les posts d'une entreprise spécifique
    @GetMapping("/getPostsByCompanyDTO/{companyId}")
    public List<PostAdminResponse> getPostsByCompanyDTO(@PathVariable("companyId") Long companyId) {
        return postService.getPostsByCompanyDTO(companyId);
    }

    @GetMapping("/getPostsByCompany/{companyId}")
    public List<Post> getPostsByCompany(@PathVariable("companyId") Long companyId) {
        return postService.getPostsByCompany(companyId);
    }

    @PostMapping("/addPostAndAffectToCompany/{companyId}")
    public ResponseEntity<?> addPostAndAffectToCompany(
            @PathVariable Long companyId,
            @RequestBody Post post,
            BindingResult result) {

        // Vérification des erreurs de validation
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        // Ajouter le post et l'affecter à l'entreprise
        Post savedPost = postService.addPostAndAffectToCompany(companyId, post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    // Supprimer un post
    @DeleteMapping("/deletePost/{postId}")
    public void deletePostById(@PathVariable("postId") Long postId) {
        postService.removePostById(postId);
    }

    // Mettre à jour un post
    @PutMapping("/updatePost")
    public Post updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    // Récupérer le fil d'actualité d'un utilisateur (les posts des entreprises suivies en premier)
    @GetMapping("/getHomeFeed/{userId}")
    public List<Post> getHomeFeed(@PathVariable("userId") Long userId) {
        return postService.getHomeFeed(userId);
    }

    /* -------------- APII META FACEBOOK --------------*/
    private final MistralAIService mistralAIService;
    @GetMapping("/analyze")
    public ResponseEntity<?> analyzeInternshipOffer(
            @RequestParam("content") String encodedContent) { // Utilisez RequestParam

        try {
            String decodedContent = URLDecoder.decode(encodedContent, StandardCharsets.UTF_8);

            // Nettoyage final
            decodedContent = decodedContent
                    .replaceAll("[\\\\/]", " ")
                    .replaceAll("[^\\p{Print}]", "")
                    .trim();

            Map<String, String> qna = mistralAIService.generateEducationalQuestions(decodedContent);

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.noCache())
                    .body(qna);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error", "Invalid request",
                            "details", e.getMessage()
                    ));
        }
    }
    /* -------------- END APII META FACEBOOK --------------*/





    @PostMapping("/byTitles")
    public List<Post> getPostsByTitles(@RequestBody List<String> titles) {
        List<Post> posts = new ArrayList<>();
        for (String title : titles) {
            posts.addAll(postService.getPostsByTitle(title));
        }
        return posts;
    }

}