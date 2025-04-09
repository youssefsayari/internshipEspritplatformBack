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

        // Sauvegarde de la Company
        return companyRepository.save(c);
    }
    @Override
    @Transactional
    public void removeCompanyByIdAndUserAffected(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));

        // Delete all posts and their dependencies in bulk
        if (!CollectionUtils.isEmpty(company.getPosts())) {
            List<Post> posts = company.getPosts();

            // Collect all related entities for bulk deletion
            List<Internship> internships = posts.stream()
                    .flatMap(post -> post.getInternships().stream())
                    .collect(Collectors.toList());

            List<Comment> comments = posts.stream()
                    .flatMap(post -> post.getComments().stream())
                    .collect(Collectors.toList());

            List<Rating> ratings = posts.stream()
                    .flatMap(post -> post.getRatings().stream())
                    .collect(Collectors.toList());

            if (!internships.isEmpty()) internshipRepository.deleteAllInBatch(internships);
            if (!comments.isEmpty()) commentRepository.deleteAllInBatch(comments);
            if (!ratings.isEmpty()) ratingRepository.deleteAllInBatch(ratings);

            postRepository.deleteAllInBatch(posts);
        }

        // Handle image
        if (company.getImage() != null) {
            try {
                cloudinaryService.delete(company.getImage().getImageId());
                imageRepository.delete(company.getImage());
            } catch (IOException e) {
                throw new ImageProcessingException("Failed to delete company image", e);
            }
        }

        // Handle followers
        if (!CollectionUtils.isEmpty(company.getFollowers())) {
            company.getFollowers().forEach(follower ->
                    follower.getFollowedCompanies().remove(company));
            userRepository.saveAll(company.getFollowers());
        }

        // Handle owner
        if (company.getOwner() != null) {
            ratingRepository.deleteByUserInBatch(company.getOwner());
            userRepository.delete(company.getOwner());
        }

        // Delete company
        companyRepository.delete(company);
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