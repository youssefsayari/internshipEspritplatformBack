package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Rating;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);

    List<Rating> findByPostId(Long postId);
    List<Rating> findByCommentId(Long commentId);

    Rating findByPostIdAndUserId(Long postId, Long userId); // Récupérer le rating d'un utilisateur pour un post
    Rating findByCommentIdAndUserId(Long commentId, Long userId);

}

