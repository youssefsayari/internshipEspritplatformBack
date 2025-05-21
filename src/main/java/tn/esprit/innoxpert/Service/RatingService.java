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
    public Rating getRatingById(Long ratingId) {
        return ratingRepository.findById(ratingId).orElse(null);
    }



    @Override
    public void removeRatingById(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    public Rating updateRating(Long postId, Long userId, int newRating) {
        // Trouver la note existante par postId et userId
        Rating existingRating = ratingRepository.findByPostIdAndUserId(postId, userId);

        if (existingRating != null) {
            // Si la note existe, la mettre à jour
            existingRating.setStars(newRating);
            return ratingRepository.save(existingRating);
        }

        // Retourner null si aucune note existante n'est trouvée
        return null;
    }

    @Override
    public Rating addRatingAndAffectToPost(Long postId, Long userId, int stars) {
        // Récupérer le post et l'utilisateur en vérifiant leur existence
        Post post = postRepository.findById(postId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        // Si le post ou l'utilisateur n'existe pas, retourner null ou lever une exception
        if (post == null || user == null) {
            throw new RuntimeException("Post ou utilisateur non trouvé");
        }

        // Créer une nouvelle note et l'associer au post et à l'utilisateur
        Rating newRating = new Rating();
        newRating.setStars(stars);
        newRating.setPost(post);
        newRating.setUser(user);

        // Sauvegarder et retourner la nouvelle note
        return ratingRepository.save(newRating);
    }






    @Override
    public Rating getMyRatingForPost(Long postId, Long userId) {
        return ratingRepository.findByPostIdAndUserId(postId, userId);
    }

    @Override

    public boolean existsByPostAndUser(Long postId, Long userId) {
        return ratingRepository.existsByUserIdAndPostId(postId, userId);
    }







}
