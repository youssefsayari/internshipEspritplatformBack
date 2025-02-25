package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Comment;
import tn.esprit.innoxpert.Service.CommentService;

import java.util.List;

@Tag(name = "Comment Management")
@RestController
@AllArgsConstructor
@RequestMapping("/comments")
public class CommentRestController {

    private final CommentService commentService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/getByPost/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getAllCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/getById/{commentId}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        return comment != null ? ResponseEntity.ok(comment) : ResponseEntity.notFound().build();
    }

    @PostMapping("/addToPost/{postId}")
    public ResponseEntity<Comment> addCommentToPost(@PathVariable Long postId, @RequestBody Comment newComment) {
        Comment savedComment = commentService.addCommentAndAffectToPost(postId, newComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @PostMapping("/addReply/{parentCommentId}")
    public ResponseEntity<Comment> addReplyToComment(@PathVariable Long parentCommentId, @RequestBody Comment newComment) {
        Comment savedComment = commentService.addCommentToComment(parentCommentId, newComment);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    @PutMapping("/update")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment updatedComment) {
        Comment savedComment = commentService.updateComment(updatedComment);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.removeCommentById(commentId);
        return ResponseEntity.noContent().build();
    }
}
