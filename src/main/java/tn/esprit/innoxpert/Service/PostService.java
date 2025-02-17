package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Post;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService implements PostServiceInterface {
    @Override
    public List<Post> getAllPosts() {
        return List.of();
    }

    @Override
    public Post getPostById(Long PostId) {
        return null;
    }

    @Override
    public Post addPost(Post p) {
        return null;
    }

    @Override
    public void removePostById(Long PostId) {

    }

    @Override
    public Post updatePost(Post p) {
        return null;
    }
}
