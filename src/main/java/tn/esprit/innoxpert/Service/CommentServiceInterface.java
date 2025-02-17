package tn.esprit.innoxpert.Service;


import tn.esprit.innoxpert.Entity.Comment;

import java.util.List;

public interface CommentServiceInterface {
    public List<Comment> getAllComments();
    public Comment getCommentById(Long CommentId);
    public Comment addComment(Comment c);
    public void removeCommentById(Long CommentId);
    public Comment updateComment (Comment c );
    public Comment addAndaffectCommentToPost(Long idPost,Comment newComment);
}
