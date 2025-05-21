package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.InternshipRemark;

import java.util.List;

@Repository
public interface InternshipRemarkRepository extends JpaRepository<InternshipRemark,Long> {
    List<InternshipRemark> findByInternship_Id(Long internshipId);
}
