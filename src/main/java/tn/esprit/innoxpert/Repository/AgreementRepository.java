package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Agreement;

import java.util.Optional;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement,Long> {
    Optional<Agreement> findByStudent_IdUser(Long studentId);

}
