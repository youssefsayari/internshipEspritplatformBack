package tn.esprit.innoxpert.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.innoxpert.Entity.Reclamation;
@Repository
public interface ReclamationRepository extends JpaRepository<Reclamation, Long>{
}
