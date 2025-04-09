package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Agreement;
import tn.esprit.innoxpert.Entity.AgreementRemark;
import tn.esprit.innoxpert.Entity.InternshipRemark;

import java.util.List;

@Repository
public interface AgreementRemarkRepository extends JpaRepository<AgreementRemark,Long> {
    List<AgreementRemark> findByAgreement_Id(Long idAgreement);
}
