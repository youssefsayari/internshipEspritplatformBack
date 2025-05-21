package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Post;
import tn.esprit.innoxpert.Entity.Rating;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rating r WHERE r.user.idUser = :userId AND r.post.id = :postId")
    boolean existsByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);



    List<Rating> findByPostId(Long postId);



    @Query("SELECT r FROM Rating r WHERE r.post.id = :postId AND r.user.idUser = :userId")
    Rating findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);


    @Modifying
    @Query("DELETE FROM Rating r WHERE r.user = :user")
    void deleteByUserInBatch(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM Rating r WHERE r.post IN :posts")
    void deleteAllByPostIn(List<Post> posts);
    @Modifying
    @Query("DELETE FROM Rating r WHERE r.user = :user")
    void deleteByUser(User user);
}

