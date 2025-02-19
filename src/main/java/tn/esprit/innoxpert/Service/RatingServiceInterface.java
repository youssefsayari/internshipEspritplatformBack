package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Rating;

import java.util.List;

public interface RatingServiceInterface {
    List<Rating> getAllRatingsByPost(Long postId);
    List<Rating> getAllRatingsByComment(Long commentId);
    Rating getRatingById(Long ratingId);
    void removeRatingById(Long ratingId);
    Rating updateRating(Rating r);

    // Ajouter un rating à un post
     Rating addRatingAndAffectToPost(Long postId, Long userId, int stars) ;

    // Ajouter un rating à un commentaire
    public Rating addRatingAndAffectToComment(Long commentId, Long userId, int stars);
    // Filtrer les ratings par post
    public Rating getMyRatingForPost(Long postId, Long userId) ;

    // Filtrer les ratings par commentaire
   Rating getMyRatingForComment(Long commentId, Long userId) ;







}

