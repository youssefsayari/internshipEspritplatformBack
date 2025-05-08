package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.PostAdminResponse;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Repository.CompanyRepository;
import tn.esprit.innoxpert.Repository.PostRepository;
import tn.esprit.innoxpert.Repository.RatingRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class PostService implements PostServiceInterface {
    @Autowired
    private PostRepository postRepository;



    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<PostAdminResponse> getAllPostsDTO() {
        List<Post> posts = postRepository.findAll();

        List<PostAdminResponse> postResponses = posts.stream().map(post -> {
            PostAdminResponse response = new PostAdminResponse();
            response.setId(post.getId());
            response.setTitle(post.getTitle());
            response.setContent(post.getContent());
            response.setCreatedAt(java.sql.Timestamp.valueOf(post.getCreatedAt()));
            response.setTypeInternship(post.getTypeInternship().name());

            if (post.getCompany() != null) {
                response.setCompanyName(post.getCompany().getName());
            }

            if (post.getSkills() != null && !post.getSkills().isEmpty()) {
                response.setSkills(new ArrayList<>(post.getSkills()));
            } else {
                response.setSkills(new ArrayList<>());
            }

            return response;
        }).toList();

        return postResponses;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<PostAdminResponse> getPostsByCompanyDTO(Long companyId) {
        User user = userRepository.findById(companyId).orElseThrow(() -> new RuntimeException("User not found"));

        Company company = companyRepository.findByOwner(user).orElseThrow(() -> new RuntimeException("Company not found"));

        List<Post> posts = postRepository.findByCompanyId(company.getId());

        List<PostAdminResponse> postResponses = posts.stream().map(post -> {
            PostAdminResponse response = new PostAdminResponse();
            response.setId(post.getId());
            response.setTitle(post.getTitle());
            response.setContent(post.getContent());
            response.setCreatedAt(java.sql.Timestamp.valueOf(post.getCreatedAt()));
            response.setTypeInternship(post.getTypeInternship().name());

            if (post.getCompany() != null) {
                response.setCompanyName(post.getCompany().getName());
            }

            if (post.getSkills() != null && !post.getSkills().isEmpty()) {
                response.setSkills(new ArrayList<>(post.getSkills()));
            } else {
                response.setSkills(new ArrayList<>());
            }

            return response;
        }).toList();

        return postResponses;
    }



    @Override
    public List<Post> getPostsByCompany(Long companyId) {
        return postRepository.findByCompanyId(companyId);

    }
    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public Post addPostAndAffectToCompany(Long companyId, Post p) {
        // Vérifier si l'entreprise existe
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        // Associer le post à l'entreprise et enregistrer
        p.setTypeInternship(TypeInternship.Graduation);
        p.setCompany(company);
        return postRepository.save(p);
    }


    @Override
    public void removePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Post updatePost(Post p) {
        // Si la date de création est modifiée dans la requête, on la garde inchangée
        Post existingPost = postRepository.findById(p.getId()).orElseThrow(() -> new RuntimeException("Post not found"));

        // Garder la date d'origine
        p.setCreatedAt(existingPost.getCreatedAt());
        p.setComments(existingPost.getComments());
        p.setRatings(existingPost.getRatings());
        p.setCompany(existingPost.getCompany());

        // Maintenant on enregistre le post avec la date inchangée
        return postRepository.save(p);
    }


    @Override
    public List<Post> getHomeFeed(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Company> followedCompanies = user.getFollowedCompanies();
        Set<Company> followedCompaniesSet = new HashSet<>(followedCompanies); // Convert List to Set

        List<Post> homeFeed = new ArrayList<>();

        // Si l'utilisateur suit des entreprises, récupérer leurs posts en priorité
        if (!followedCompanies.isEmpty()) {
            homeFeed.addAll(postRepository.findByCompanyInOrderByCreatedAtDesc(followedCompaniesSet));
        }

        // Ajouter les autres posts (hors entreprises suivies)
        homeFeed.addAll(postRepository.findByCompanyNotInOrderByCreatedAtDesc(followedCompaniesSet));

        return homeFeed;
    }

    @Override
    public List<Post> getPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title);
    }

}
