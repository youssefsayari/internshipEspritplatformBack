package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Comment;

@Repository

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
