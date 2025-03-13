package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Agreement;
import tn.esprit.innoxpert.Entity.Company;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement,Long> {
}
