package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.User;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    Optional<Company> findByOwner(User owner);

    @Query("SELECT c FROM Company c WHERE c.owner.idUser = :userId")
    Company findByOwnerId(Long userId);

    default String generatePassword(Company company) {
        return company.getName() + "_" + company.getSecretKey();
    }
}
