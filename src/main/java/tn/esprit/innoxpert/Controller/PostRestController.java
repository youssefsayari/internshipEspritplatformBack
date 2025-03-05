package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.Post;
import tn.esprit.innoxpert.Service.PostServiceInterface;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.validation.ObjectError;


@Tag(name = "Post Management")
@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostRestController {

    @Autowired
    PostServiceInterface postService;


    // Récupérer tous les posts
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
    @GetMapping("/getPostsByCompany/{companyId}")
    public List<Post> getPostsByCompany(@PathVariable("companyId") Long companyId) {
        return postService.getPostsByCompany(companyId);
    }

    // Ajouter un post avec une image et l'affecter à une entreprise
    @PostMapping("/addPostAndAffectToCompany/{companyId}")
    public ResponseEntity<?> addPostAndAffectToCompany(
            @PathVariable Long companyId,
            @RequestBody  Post post,
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


}
