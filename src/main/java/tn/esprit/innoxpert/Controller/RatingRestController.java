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



    @GetMapping("/getRatingById/{ratingId}")
    public ResponseEntity<Rating> getRatingById(@PathVariable Long ratingId) {
        return ResponseEntity.ok(ratingService.getRatingById(ratingId));
    }

    @DeleteMapping("/deleteRating/{ratingId}")
    public ResponseEntity<Void> deleteRatingById(@PathVariable Long ratingId) {
        ratingService.removeRatingById(ratingId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{postId}/{userId}")
    public ResponseEntity<Rating> updateRating(@PathVariable Long postId,
                                               @PathVariable Long userId,
                                               @RequestBody Rating rating) {
        // Mettre à jour la note dans la base de données
        Rating updatedRating = ratingService.updateRating(postId, userId, rating.getStars());

        // Vérifier si l'évaluation a été mise à jour avec succès
        if (updatedRating != null) {
            return ResponseEntity.ok(updatedRating);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/addRatingToPost/{postId}/{userId}/{stars}")
    public ResponseEntity<?> addRatingToPost(@PathVariable Long postId, @PathVariable Long userId, @PathVariable int stars) {
        try {
            // Tentative de récupérer la note existante pour ce post et utilisateur
            Rating existingRating = ratingService.updateRating(postId, userId, stars);

            if (existingRating != null) {
                // Si une note existante a été mise à jour
                System.out.println("Updated rating for postId: " + postId + ", userId: " + userId + ", stars: " + stars);
                return ResponseEntity.ok(existingRating);
            } else {
                // Si aucune note n'existait, on crée une nouvelle note
                System.out.println("Adding new rating for postId: " + postId + ", userId: " + userId + ", stars: " + stars);
                Rating newRating = ratingService.addRatingAndAffectToPost(postId, userId, stars);
                return ResponseEntity.ok(newRating);
            }
        } catch (RuntimeException e) {
            // Gérer les erreurs
            System.err.println("Error adding or updating rating: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding or updating rating: " + e.getMessage());
        }
    }






    @GetMapping("/getMyRatingForPost/{postId}/{userId}")
    public ResponseEntity<Rating> getMyRatingForPost(@PathVariable Long postId, @PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getMyRatingForPost(postId, userId));
    }

    @GetMapping("/hasRated/{postId}/{userId}")
    public boolean hasUserRated(@PathVariable Long postId, @PathVariable Long userId) {
        return ratingService.existsByPostAndUser(postId, userId);
    }


}
