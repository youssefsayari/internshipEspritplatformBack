package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Post;

import java.util.List;

public interface PostServiceInterface {
    public List<Post> getAllPosts();
    public Post getPostById(Long PostId);
    public Post addPost(Post p);
    public void removePostById(Long PostId);
    public Post updatePost (Post p );
}
