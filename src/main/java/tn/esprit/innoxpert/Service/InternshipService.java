package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.AddInternship;
import tn.esprit.innoxpert.DTO.InternshipResponse;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.InternshipRepository;
import tn.esprit.innoxpert.Repository.PostRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class InternshipService implements InternshipServiceInterface {
    @Autowired
    InternshipRepository internshipRepository;
    UserRepository userRepository;
    PostRepository postRepository;

    @Override
    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }

    @Override
    public List<InternshipResponse> getInternshipsByCriteria(Long idUser, Long idPost) {
        if (idUser != null) {
            return internshipRepository.findByUsers_IdUser(idUser).stream()
                    .map(this::mapToInternshipResponse)
                    .toList();
        } else if (idPost != null) {
            return internshipRepository.findByPost_Id(idPost).stream()
                    .map(this::mapToInternshipResponse)
                    .toList();
        } else {
            throw new IllegalArgumentException("Au moins un critère (idUser ou idPost) doit être fourni.");
        }
    }

    private InternshipResponse mapToInternshipResponse(Internship internship) {
        return new InternshipResponse(
                internship.getId(),
                internship.getTitle(),
                internship.getDescription(),
                internship.getInternshipState()
        );
    }


    @Override
    public Internship getInternshipById(Long internshipId) {
        return internshipRepository.findById(internshipId)
                .orElseThrow(() -> new NotFoundException("Meeting with ID : " + internshipId + " was not found."));
    }

    @Override
    public void addInternship(AddInternship addInternship) {
        User user = userRepository.findById(addInternship.getIdUser())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (user.getTypeUser() != TypeUser.Student) {
            throw new RuntimeException("Only students can apply for an internship.");
        }

        Document cvDocument = user.getDocuments().stream()
                .filter(doc -> "CV".equalsIgnoreCase(String.valueOf(doc.getTypeDocument())))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User does not have a CV document."));


        Post post = postRepository.findById(addInternship.getIdPost())
                .orElseThrow(() -> new NotFoundException("Post not found"));
        String userClasse = user.getClasse();

        if (userClasse == null || userClasse.isEmpty()) {
            throw new RuntimeException("User class information is missing.");
        }

        boolean alreadyApplied = internshipRepository.existsByPostAndUsersContains(post, user);
        if (alreadyApplied) {
            throw new RuntimeException("You have already applied for this internship.");
        }

        if ("1234".contains(userClasse.substring(0, 1))) {
            if (!post.getTypeInternship().equals(TypeInternship.Summer))
            {
                throw new RuntimeException("You can only apply for a Summer internship.");
            }
        } else if (userClasse.startsWith("5")) {
            if (!post.getTypeInternship().equals(TypeInternship.Graduation))
            {
                throw new RuntimeException("You can only apply for a Graduation internship.");
            }
        } else {
            throw new RuntimeException("Invalid user class: " + userClasse);
        }

        Internship internship = new Internship();
        internship.setTitle(post.getTitle());
        internship.setDescription(post.getContent());
        internship.setInternshipState(InternshipState.PENDING);
        internship.setId_document(cvDocument.getId());
        internship.setPost(post);
        internship.getUsers().add(user);
        internshipRepository.save(internship);
    }

    @Override
    public void removeInternshipById(Long internshipId) {
        if (!internshipRepository.existsById(internshipId)) {
            throw new NotFoundException("Meeting with ID :  " + internshipId + " was not found.");
        }
        internshipRepository.deleteById(internshipId);
    }

    @Override
    public Internship updateInternship(Internship b) {
        return null;
    }

    @Override
    public Internship approveInternship(Long internshipId) {
        return null;
    }

    @Override
    public Internship rejectInternship(Long internshipId) {
        return null;
    }

    @Override
    public Map<String, Object> getInternshipStatistics() {
        return null;
    }

    @Override
    public Internship GenerateInternshipcertificate(Long interbshipId) {
        return null;
    }

}



