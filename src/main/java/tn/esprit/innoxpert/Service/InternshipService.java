package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.AddInternship;
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
    public List<Internship> getInternshipsByCriteria(Long idUser, Long idPost) {
        if (idUser != null) {
            return internshipRepository.findAll().stream()
                    .filter(internship -> internship.getUsers().stream()
                            .anyMatch(user -> user.getIdUser().equals(idUser)))
                    .toList();
        }
        else if (idPost != null) {
            return internshipRepository.findAll().stream()
                    .filter(internship -> internship.getPost() != null && internship.getPost().getId().equals(idPost))
                    .toList();
        } else {
            throw new IllegalArgumentException("Au moins un critère (idUser ou idPost) doit être fourni.");
        }
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
    public Internship affectationTutor(Long internshipId) {
        return null;
    }

    @Override
    public Internship GenerateInternshipcertificate(Long interbshipId) {
        return null;
    }

}



