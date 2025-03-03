package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.PostAdminResponse;
import tn.esprit.innoxpert.Entity.Post;
import tn.esprit.innoxpert.Service.PostServiceInterface;

import java.util.List;

@Tag(name = "Post Management")
@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostRestController {

    @Autowired
    PostServiceInterface postService;

    // Récupérer tous les posts
    @GetMapping("/getAllPosts")
    public List<PostAdminResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    // Récupérer un post par ID
    @GetMapping("/getPostById/{postId}")
    public Post getPostById(@PathVariable("postId") Long postId) {
        return postService.getPostById(postId);
    }

    // Récupérer les posts d'une entreprise spécifique
    @GetMapping("/getPostsByCompany/{companyId}")
    public List<PostAdminResponse> getPostsByCompany(@PathVariable("companyId") Long companyId) {
        return postService.getPostsByCompany(companyId);
    }

    // Ajouter un post et l'affecter à une entreprise
    @PostMapping("/addPostAndAffectToCompany/{companyId}")
    public Post addPostAndAffectToCompany(@PathVariable("companyId") Long companyId, @RequestBody Post post) {
        return postService.addPostAndAffectToCompany(companyId, post);
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