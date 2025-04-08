package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.innoxpert.Entity.Agreement;

public interface AgreementRepository extends JpaRepository<Agreement,Long> {
}
