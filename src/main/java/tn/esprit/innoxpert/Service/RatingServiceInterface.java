package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Rating;

import java.util.List;

public interface RatingServiceInterface {
    List<Rating> getAllRatingsByPost(Long postId);

    Rating getRatingById(Long ratingId);
    void removeRatingById(Long ratingId);
    public Rating updateRating(Long postId, Long userId, int newRating) ;

    // Ajouter un rating à un post
     Rating addRatingAndAffectToPost(Long postId, Long userId, int stars) ;


    // Filtrer les ratings par post
    public Rating getMyRatingForPost(Long postId, Long userId) ;
    public boolean existsByPostAndUser(Long postId, Long userId) ;









}

