package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Societe;

@Repository
public interface SocieteRepository extends JpaRepository<Societe,Long> {
}
