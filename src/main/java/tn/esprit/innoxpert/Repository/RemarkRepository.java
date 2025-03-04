package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.innoxpert.Entity.InternshipRemark;
import tn.esprit.innoxpert.Entity.Post;

public interface RemarkRepository extends JpaRepository<InternshipRemark,Long> {
}
