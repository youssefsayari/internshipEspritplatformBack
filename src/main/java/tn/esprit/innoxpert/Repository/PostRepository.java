package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.Post;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByCompanyId(Long companyId);
    List<Post> findByCompanyInOrderByCreatedAtDesc(Set<Company> companies);
    List<Post> findByCompanyNotInOrderByCreatedAtDesc(Set<Company> companies);

}
