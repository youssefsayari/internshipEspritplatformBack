package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.AgreementRemark;
import tn.esprit.innoxpert.Entity.Client;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

}
