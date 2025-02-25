package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Rating;
import tn.esprit.innoxpert.Service.RatingServiceInterface;

import java.util.List;

@Tag(name = "Rating Management")
@RestController
@AllArgsConstructor
@RequestMapping("/rating")
public class RatingRestController {

    private final RatingServiceInterface ratingService;

    @GetMapping("/getAllRatingsByPost/{postId}")
    public ResponseEntity<List<Rating>> getAllRatingsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(ratingService.getAllRatingsByPost(postId));
    }

    @GetMapping("/getAllRatingsByComment/{commentId}")
    public ResponseEntity<List<Rating>> getAllRatingsByComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(ratingService.getAllRatingsByComment(commentId));
    }

    @GetMapping("/getRatingById/{ratingId}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long ratingId) {
        return ResponseEntity.ok(ratingService.getRatingById(ratingId));
    }

    @DeleteMapping("/deleteRating/{ratingId}")
    public ResponseEntity<Void> deleteRatingById(@PathVariable Long ratingId) {
        ratingService.removeRatingById(ratingId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/updateRating")
    public ResponseEntity<Rating> updateRating(@RequestBody Rating rating) {
        return ResponseEntity.ok(ratingService.updateRating(rating));
    }

    @PostMapping("/addRatingToPost/{postId}/{userId}/{stars}")
    public ResponseEntity<?> addRatingToPost(@PathVariable Long postId, @PathVariable Long userId, @PathVariable int stars) {
        try {
            return ResponseEntity.ok(ratingService.addRatingAndAffectToPost(postId, userId, stars));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/addRatingToComment/{commentId}/{userId}/{stars}")
    public ResponseEntity<?> addRatingToComment(@PathVariable Long commentId, @PathVariable Long userId, @PathVariable int stars) {
        try {
            return ResponseEntity.ok(ratingService.addRatingAndAffectToComment(commentId, userId, stars));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getMyRatingForPost/{postId}/{userId}")
    public ResponseEntity<Rating> getMyRatingForPost(@PathVariable Long postId, @PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getMyRatingForPost(postId, userId));
    }

    @GetMapping("/getMyRatingForComment/{commentId}/{userId}")
    public ResponseEntity<Rating> getMyRatingForComment(@PathVariable Long commentId, @PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getMyRatingForComment(commentId, userId));
    }
}
