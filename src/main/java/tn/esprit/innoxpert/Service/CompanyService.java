package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Repository.CompanyRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService implements CompanyServiceInterface {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId).orElse(null);
    }

    @Override
    public Company addCompanyAndAffectToNewUser(Company c) {
        // Création automatique du User
        User user = new User();
        user.setFirstName(c.getName());
        user.setLastName("Admin"); // Peut être modifié selon les besoins
        user.setIdentifiant(c.getEmail()); // Identifiant = email de la company
        user.setTypeUser(TypeUser.Company); // Type d'utilisateur = Company
        user.setEmail(c.getEmail());

        if (c.getPhone() != null && !c.getPhone().isEmpty()) {
            user.setTelephone(Long.parseLong(c.getPhone()));
        }

        // Génération du mot de passe sans encodage
        String rawPassword = companyRepository.generatePassword(c);
        user.setPassword(rawPassword);

        // Sauvegarde du User
        user = userRepository.save(user);

        // Associer le User à la Company
        c.setOwner(user);

        // Sauvegarde de la Company
        return companyRepository.save(c);
    }

    @Override
    public void removeCompanyByIdAndUserAffected(Long companyId) {
        if (companyRepository.existsById(companyId)) {
            Company company = companyRepository.findById(companyId).orElse(null);
            if (company != null) {
                // Supprimer l'utilisateur lié
                userRepository.delete(company.getOwner());

                // Supprimer l'entreprise
                companyRepository.deleteById(companyId);
            }
        }
    }

    @Override
    public Company updateCompany(Company c) {
        return companyRepository.save(c);
    }









    @Override
    public List<User> getCompanyFollowers(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        return company.getFollowers();
    }


}
