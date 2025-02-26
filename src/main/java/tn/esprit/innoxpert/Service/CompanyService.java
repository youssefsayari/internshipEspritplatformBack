package tn.esprit.innoxpert.Service;

import jakarta.transaction.Transactional;
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

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId).orElse(null);
    }

    @Override
    @Transactional
    public Company addCompanyAndAffectToNewUser(Company c) {
        // Création automatique du User
        User user = new User();
        user.setFirstName(c.getName());
        user.setLastName("Admin"); // Peut être modifié selon les besoins
        user.setIdentifiant(c.getEmail()); // Identifiant = email de la company
        user.setTypeUser(TypeUser.Company); // Type d'utilisateur = Company
        user.setEmail(c.getEmail());
        user.setTelephone(c.getPhone());

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
    @Transactional
    public void removeCompanyByIdAndUserAffected(Long companyId) {
        if (companyRepository.existsById(companyId)) {
            Company company = companyRepository.findById(companyId).orElse(null);
            if (company != null) {
                // Supprimer le propriétaire de l'entreprise
                if (company.getOwner() != null) {
                    userRepository.delete(company.getOwner());
                }

                // Supprimer tous les followers de cette entreprise
                if (company.getFollowers() != null) {
                    for (User follower : company.getFollowers()) {
                        follower.getFollowedCompanies().remove(company);
                        userRepository.save(follower); // Mettre à jour les followers
                    }
                }

                // Supprimer l'entreprise
                companyRepository.delete(company);
            }
        }
    }

    @Override
    @Transactional
    public Company updateCompany(Company c) {
        Company existingCompany = companyRepository.findById(c.getId())
                .orElseThrow(() -> new RuntimeException("Company not found"));

        // Mise à jour des champs autorisés (on ne touche pas aux relations)
        existingCompany.setName(c.getName());
        existingCompany.setAbbreviation(c.getAbbreviation());
        existingCompany.setAddress(c.getAddress());
        existingCompany.setSector(c.getSector());
        existingCompany.setEmail(c.getEmail());
        existingCompany.setPhone(c.getPhone());
        existingCompany.setFoundingYear(c.getFoundingYear());
        existingCompany.setLabelDate(c.getLabelDate());
        existingCompany.setWebsite(c.getWebsite());
        existingCompany.setFounders(c.getFounders());

        // On ne touche pas aux relations : posts, owner, followers
        return companyRepository.save(existingCompany);
    }

    @Override
    public List<User> getCompanyFollowers(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return company.getFollowers();
    }





    @Override
    @Transactional
    public void followCompany(Long userId, Long companyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (!user.getFollowedCompanies().contains(company)) {
            user.getFollowedCompanies().add(company);
            company.getFollowers().add(user);
            userRepository.save(user);
            companyRepository.save(company);
        } else {
            throw new RuntimeException("User is already following this company");
        }
    }

    @Override
    @Transactional
    public void unfollowCompany(Long userId, Long companyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (user.getFollowedCompanies().contains(company)) {
            user.getFollowedCompanies().remove(company);
            company.getFollowers().remove(user);
            userRepository.save(user);
            companyRepository.save(company);
        } else {
            throw new RuntimeException("User is not following this company");
        }
    }

}