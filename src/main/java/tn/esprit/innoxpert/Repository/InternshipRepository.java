package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.Post;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship,Long> {
    List<Internship> findByUsers_IdUser(Long idUser);
    List<Internship> findByPost_Id(Long idPost);
    boolean existsByPostAndUsersContains(Post post, User user);

}
