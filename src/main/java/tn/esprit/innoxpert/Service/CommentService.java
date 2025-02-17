package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Comment;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService implements CommentServiceInterface {
    @Override
    public List<Comment> getAllComments() {
        return List.of();
    }

    @Override
    public Comment getCommentById(Long CommentId) {
        return null;
    }

    @Override
    public Comment addComment(Comment c) {
        return null;
    }

    @Override
    public void removeCommentById(Long CommentId) {

    }

    @Override
    public Comment updateComment(Comment c) {
        return null;
    }

    @Override
    public Comment addAndaffectCommentToPost(Long idPost, Comment newComment) {
        return null;
    }
}
