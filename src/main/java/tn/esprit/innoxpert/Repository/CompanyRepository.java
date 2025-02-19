package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.Post;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    // Méthode par défaut pour générer le mot de passe d'une entreprise
    default String generatePassword(Company company) {
        return company.getName() + "_" + company.getSecretKey();
    }
}
