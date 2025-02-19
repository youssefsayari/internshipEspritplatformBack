package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Comment;
import tn.esprit.innoxpert.Entity.Comment;
import tn.esprit.innoxpert.Entity.Rating;
import tn.esprit.innoxpert.Repository.CommentRepository;
import tn.esprit.innoxpert.Repository.PostRepository;
import tn.esprit.innoxpert.Repository.RatingRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService implements CommentServiceInterface {

    @Autowired
    private CommentRepository commentRepository;



    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getAllCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }


    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }



    @Override
    public void removeCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment updateComment(Comment c) {
        return commentRepository.save(c);
    }

    @Override
    public Comment addCommentAndAffectToPost(Long idPost, Comment newComment) {
        // Logique pour ajouter un commentaire à un post
        newComment.setPost(postRepository.findById(idPost).orElse(null));
        return commentRepository.save(newComment);
    }

    @Override
    public Comment addCommentToComment(Long parentCommentId, Comment newComment) {
        Comment parentComment = commentRepository.findById(parentCommentId).get();


        newComment.setParentComment(parentComment);
        return commentRepository.save(newComment);
    }
}
