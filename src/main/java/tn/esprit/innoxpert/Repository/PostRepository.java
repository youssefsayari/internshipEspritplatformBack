package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
}
