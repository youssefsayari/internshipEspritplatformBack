package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Client;
import tn.esprit.innoxpert.Entity.Consultant;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant,Long> {

    boolean existsByEmailAndIdNot(String email, Long excludeId);

    boolean existsByEmail(String email);

    Consultant findByEmail(String email);
}
