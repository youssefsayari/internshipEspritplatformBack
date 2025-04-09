package tn.esprit.innoxpert.Service;

import java.io.IOException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Exceptions.ImageProcessingException;
import tn.esprit.innoxpert.Exceptions.ResourceNotFoundException;
import tn.esprit.innoxpert.Repository.*;
import tn.esprit.innoxpert.Util.EmailClass;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService implements CompanyServiceInterface {
    @Autowired

    private final CompanyRepository companyRepository;
    @Autowired

    private final UserRepository userRepository;
    @Autowired
    private final PostRepository postRepository;
    @Autowired
    private final RatingRepository ratingRepository;
    @Autowired

    private final CloudinaryService cloudinaryService;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserService userService;
    @Autowired

    private InternshipRepository internshipRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long companyId) {
        if (companyId == null || companyId <= 0) {
            throw new IllegalArgumentException("Invalid company ID");
        }
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));
    }
    @Override
    @Transactional
    public Company addCompanyAndAffectToNewUser(Company c, MultipartFile file) throws IOException {
        // Création automatique du User
        User user = new User();
        user.setFirstName(c.getName());
        user.setLastName("(Company)"); // Peut être modifié selon les besoins
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

        // 1. Upload de l'image vers Cloudinary
        Map<String, String> uploadResult = cloudinaryService.upload(file);

        // 2. Création de l'entité Image
        Image image = new Image();
        image.setName(uploadResult.get("original_filename"));
        image.setImageUrl(uploadResult.get("url"));
        image.setImageId(uploadResult.get("public_id"));

        // 3. Sauvegarde de l'image dans la base de données
        image = imageRepository.save(image); // Assurez-vous que l'image a un ID généré

        // Associer l'image à l'entreprise
        c.setImage(image);


        // Envoi de l'email avec les identifiants
        EmailClass emailSender = new EmailClass();
        emailSender.sendCompanyCredentialsEmail(
                c.getEmail(),
                c.getName(),
                user.getIdentifiant(),
                rawPassword
        );

        // Sauvegarde de la Company
        return companyRepository.save(c);
    }
    @Override
    @Transactional
    public void removeCompanyByIdAndUserAffected(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        try {
            // 1. Handle quizzes first (added based on your entity model)
            if (!CollectionUtils.isEmpty(company.getQuizzes())) {
                company.getQuizzes().forEach(quiz -> {
                    if (!CollectionUtils.isEmpty(quiz.getQuestions())) {
                        quiz.getQuestions().clear();
                    }
                });
                company.getQuizzes().clear();
                companyRepository.save(company); // Save to clear relationships
            }

            // 2. Handle posts and their dependencies
            if (!CollectionUtils.isEmpty(company.getPosts())) {
                // Delete comments first
                commentRepository.deleteAllByPostIn(company.getPosts());
                // Delete ratings
                ratingRepository.deleteAllByPostIn(company.getPosts());
                // Delete internships
                internshipRepository.deleteAllByPostIn(company.getPosts());
                // Finally delete posts
                postRepository.deleteAll(company.getPosts());
                company.getPosts().clear();
            }

            // 3. Handle followers
            if (!CollectionUtils.isEmpty(company.getFollowers())) {
                company.getFollowers().forEach(follower ->
                        follower.getFollowedCompanies().remove(company));
                userRepository.saveAll(company.getFollowers());
                company.getFollowers().clear();
            }

            // 4. Handle image
            if (company.getImage() != null) {
                try {
                    cloudinaryService.delete(company.getImage().getImageId());
                    imageRepository.delete(company.getImage());
                } catch (IOException e) {
                    // Log but don't fail if image deletion fails
                    System.err.println("Warning: Failed to delete company image: " + e.getMessage());
                }
            }

            // 5. Handle owner
            if (company.getOwner() != null) {
                // Delete ratings by this user
                ratingRepository.deleteByUser(company.getOwner());
                // Delete the user
                userRepository.delete(company.getOwner());
            }

            // 6. Finally delete the company
            companyRepository.delete(company);

        } catch (Exception e) {
            System.err.println("Error during company deletion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete company: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Company updateCompany(Long companyId, Company updatedData) {
        Company existingCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId));

        // Validation des données
        if (updatedData.getName() == null || updatedData.getName().isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }
        // Mise à jour des champs autorisés (on ne touche pas aux relations)
        existingCompany.setName(updatedData.getName());
        existingCompany.setAbbreviation(updatedData.getAbbreviation());
        existingCompany.setAddress(updatedData.getAddress());
        existingCompany.setSector(updatedData.getSector());
        existingCompany.setEmail(updatedData.getEmail());
        existingCompany.setPhone(updatedData.getPhone());
        existingCompany.setFoundingYear(updatedData.getFoundingYear());
        existingCompany.setLabelDate(updatedData.getLabelDate());
        existingCompany.setWebsite(updatedData.getWebsite());
        existingCompany.setFounders(updatedData.getFounders());
        User user=userService.getUserById(existingCompany.getOwner().getIdUser());
        user.setEmail(updatedData.getEmail());
        user.setFirstName(updatedData.getName());
        user.setIdentifiant(updatedData.getEmail());
        user.setTelephone(updatedData.getPhone());
        userRepository.save(user);

// Envoi de l'email avec les identifiants mis à jour
        EmailClass emailSender = new EmailClass();
        emailSender.sendCompanyCredentialsEmail(
                user.getEmail(),
                existingCompany.getName(),
                user.getIdentifiant(),
                user.getPassword()
        );
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
    public List<Company> getCompaniesFollowedByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFollowedCompanies();
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
    @Override
    public boolean isUserFollowingCompany(Long userId, Long companyId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        return user.getFollowedCompanies().contains(company);
    }

    @Override
    public Long getCompanyIdByUserId(Long userId) {
        Company company = companyRepository.findByOwnerId(userId);
        if (company != null) {
            return company.getId();
        }
        return null; // or throw an exception if company not found
    }

    @Override
    public Boolean IsCompany(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && TypeUser.Company.equals(user.getTypeUser())) {
            return true;
        }
        return false; // or throw an exception if company not found
    }
    public Company updateCompanyImage(Long companyId, Image image) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setImage(image);
        return companyRepository.save(company);
    }
    public Company getCompanyByUserId(Long userId) {
        return companyRepository.findByOwnerId(userId);
    }


}