package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.InternshipState;
import tn.esprit.innoxpert.Entity.Post;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship,Long> {
    List<Internship> findByUsers_IdUser(Long idUser);
    List<Internship> findByPost_Id(Long idPost);
    boolean existsByPostAndUsersContains(Post post, User user);

    @Query("SELECT COUNT(i) > 0 FROM Internship i JOIN i.users u WHERE u.idUser = :studentId AND i.internshipState = :state")
    boolean existsByStudentIdAndState(@Param("studentId") Long studentId, @Param("state") InternshipState state);

    @Query("SELECT i FROM Internship i JOIN i.users u WHERE u.idUser = :studentId AND i.internshipState = :state")
    List<Internship> findInternshipsByStudentIdAndState(@Param("studentId") Long studentId, @Param("state") InternshipState state);

    @Modifying
    @Query("DELETE FROM Internship i WHERE i.post IN :posts")
    void deleteAllByPostIn(List<Post> posts);


}
