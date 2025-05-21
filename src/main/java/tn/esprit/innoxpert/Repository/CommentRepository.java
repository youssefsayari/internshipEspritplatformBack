package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Comment;
import tn.esprit.innoxpert.Entity.Post;

import java.util.List;

@Repository

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.post IN :posts")
    void deleteAllByPostIn(List<Post> posts);

}
