package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.DTO.PostAdminResponse;
import tn.esprit.innoxpert.Entity.Post;

import java.util.List;

public interface PostServiceInterface {
    List<PostAdminResponse> getAllPostsDTO();
    List<PostAdminResponse> getPostsByCompanyDTO(Long companyId);
    Post getPostById(Long postId);
    List<Post> getAllPosts();
    List<Post> getPostsByCompany(Long companyId);
    Post addPostAndAffectToCompany(Long companyId,Post p);
    void removePostById(Long postId);
    Post updatePost(Post p);
    List<Post> getHomeFeed(Long idUser);
    public List<Post> getPostsByTitle(String title);


}