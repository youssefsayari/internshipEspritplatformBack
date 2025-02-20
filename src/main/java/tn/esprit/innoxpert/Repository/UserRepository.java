package tn.esprit.innoxpert.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.User;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByIdentifiant(String identifiant);



}
