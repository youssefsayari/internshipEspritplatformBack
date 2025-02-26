package tn.esprit.innoxpert.Service;


import tn.esprit.innoxpert.Entity.Comment;
import tn.esprit.innoxpert.Entity.Rating;

import java.util.List;

public interface CommentServiceInterface {
    List<Comment> getAllComments();
    List<Comment> getAllCommentsByPostId(Long postId);
    Comment getCommentById(Long commentId);
    void removeCommentById(Long commentId);
    Comment updateComment(Comment c);
    Comment addCommentAndAffectToPost(Long idPost, Comment newComment);
    Comment addCommentToComment(Long parentCommentId, Comment newComment);






    }

