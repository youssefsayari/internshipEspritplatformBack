package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.PostAdminResponse;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.Post;
import tn.esprit.innoxpert.Entity.Rating;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Repository.CompanyRepository;
import tn.esprit.innoxpert.Repository.PostRepository;
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
    public List<PostAdminResponse> getAllPosts() {
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


    @Override
    public List<Post> getPostsByCompany(Long companyId) {
        return postRepository.findByCompanyId(companyId);

    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public Post addPostAndAffectToCompany(Long companyId,Post p) {
        Company company = companyRepository.findById(companyId).get();
        p.setCompany(company); // Associer le post à la company
        return postRepository.save(p);
    }

    @Override
    public void removePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public Post updatePost(Post p) {
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



}