package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Comment;
import tn.esprit.innoxpert.Entity.Post;
import tn.esprit.innoxpert.Entity.Rating;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Repository.CommentRepository;
import tn.esprit.innoxpert.Repository.PostRepository;
import tn.esprit.innoxpert.Repository.RatingRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RatingService implements RatingServiceInterface{
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Rating> getAllRatingsByPost(Long postId) {
        return ratingRepository.findByPostId(postId);  // Filtrer par ID du post
    }

    @Override
    public List<Rating> getAllRatingsByComment(Long commentId) {
        return ratingRepository.findByCommentId(commentId);  // Filtrer par ID du post
    }


    @Override
    public Rating getRatingById(Long ratingId) {
        return ratingRepository.findById(ratingId).orElse(null);
    }



    @Override
    public void removeRatingById(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    @Override
    public Rating updateRating(Rating r) {
        return ratingRepository.save(r);
    }

    @Override
    public Rating addRatingAndAffectToPost(Long postId, Long userId, int stars) {
        // Vérifier si l'utilisateur a déjà donné un rating pour ce post
        if (ratingRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new RuntimeException("User has already rated this post.");
        }

        // Récupérer le post et l'utilisateur
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findById(userId).get();

        // Créer et associer le rating
        Rating newRating = new Rating();
        newRating.setStars(stars);
        newRating.setPost(post);
        newRating.setUser(user);

        // Sauvegarder et retourner le rating
        return ratingRepository.save(newRating);
    }


    @Override
    public Rating addRatingAndAffectToComment(Long commentId, Long userId, int stars) {
        // Vérifier si l'utilisateur a déjà donné un rating pour ce commentaire
        if (ratingRepository.existsByUserIdAndCommentId(userId, commentId)) {
            throw new RuntimeException("User has already rated this comment.");
        }

        // Récupérer le commentaire et l'utilisateur
        Comment comment = commentRepository.findById(commentId).get();
        User user = userRepository.findById(userId).get();

        // Créer et associer le rating
        Rating newRating = new Rating();
        newRating.setStars(stars);
        newRating.setComment(comment);
        newRating.setUser(user);

        // Sauvegarder et retourner le rating
        return ratingRepository.save(newRating);
    }


    @Override
    public Rating getMyRatingForPost(Long postId, Long userId) {
        return ratingRepository.findByPostIdAndUserId(postId, userId);
    }


    @Override
    public Rating getMyRatingForComment(Long commentId, Long userId) {
        return ratingRepository.findByCommentIdAndUserId(commentId, userId);
    }



}
