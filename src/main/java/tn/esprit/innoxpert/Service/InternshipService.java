package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.AddInternship;
import tn.esprit.innoxpert.DTO.InternshipAdminResponse;
import tn.esprit.innoxpert.DTO.InternshipResponse;
import tn.esprit.innoxpert.DTO.InternshipTutorResponse;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.InternshipRepository;
import tn.esprit.innoxpert.Repository.PostRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<InternshipAdminResponse> getInternshipsForAdmin(Long idPost) {
        Post post = postRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Post not found"));
        List<Internship> internships = post.getInternships();

        List<InternshipAdminResponse> responseList = internships.stream().map(internship -> {
            InternshipAdminResponse response = new InternshipAdminResponse();
            response.setIdInternship(internship.getId());
            if (!internship.getUsers().isEmpty()) {
                String studentName = internship.getUsers().get(0).getFirstName() + " " + internship.getUsers().get(0).getLastName();
                response.setStudentName(studentName);
                response.setClasse(internship.getUsers().get(0).getClasse());
                response.setIdStudent(internship.getUsers().get(0).getIdUser());
            }

                User user = internship.getUsers().get(0);
            if (user.getTutor() != null) {
                String tutorName = user.getTutor().getFirstName() + " " + user.getTutor().getLastName();
                response.setTutorName(tutorName);
                response.setIdTutor(user.getTutor().getIdUser());
            }else {
                response.setTutorName("No tutor assigned");
            }
            response.setInternshipState(internship.getInternshipState().name());

            if (internship.getValidator() != null) {
                response.setValidator_id(internship.getValidator().getIdUser());
                String validatorName = internship.getValidator().getFirstName() + " " + internship.getValidator().getLastName();
                response.setValidatorName(validatorName);
            }else {
                response.setValidatorName("No tutor assigned");
            }

            return response;
        }).collect(Collectors.toList());

        return responseList;
    }

    @Override
    public void affectationValidator(Long internshipId, Long tutorId) {
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        User validator = userRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor not found"));

        internship.setValidator(validator);
        internshipRepository.save(internship);
    }


    @Override
    public Internship updateInternship(Internship b) {
        return null;
    }

    @Override
    public void approveInternship(Long internshipId) {
        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));
        if (!(internship.getInternshipState().equals(InternshipState.PENDING) ||
                internship.getInternshipState().equals(InternshipState.APPROVEDBYCOMPANY))) {
            throw new RuntimeException("Internship is not in a state that can be approved.");
        }
        if (internship.getInternshipState().equals(InternshipState.APPROVEDBYCOMPANY)) {
            internship.setInternshipState(InternshipState.APPROVED);
        } else {
            internship.setInternshipState(InternshipState.APPROVEDBYCOMPANY);
        }

        internshipRepository.save(internship);
    }




    @Override
    public void rejectInternship(Long internshipId) {

        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        if (!(internship.getInternshipState().equals(InternshipState.PENDING) ||
                internship.getInternshipState().equals(InternshipState.APPROVEDBYCOMPANY))) {
            throw new RuntimeException("Internship is not in a state that can be approved.");
        }

        internship.setInternshipState(InternshipState.REJECTED);
        internshipRepository.save(internship);
    }

    @Override
    public List<InternshipTutorResponse> getInternshipsForTutor(Long idUser) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> students = userRepository.findByTutor_IdUser(idUser);
        List<Internship> internshipsAsTutor = new ArrayList<>();

        for (User student : students) {
            internshipsAsTutor.addAll(student.getInternships());
        }

        List<Internship> internshipsAsValidator = internshipRepository.findByValidator_IdUser(idUser);

        Set<Internship> allInternships = new HashSet<>();
        allInternships.addAll(internshipsAsTutor);
        allInternships.addAll(internshipsAsValidator);

        return allInternships.stream().map(internship -> {
            InternshipTutorResponse response = new InternshipTutorResponse();
            response.setIdInternship(internship.getId());

            if (!internship.getUsers().isEmpty()) {
                User student = internship.getUsers().get(0);
                response.setStudentName(student.getFirstName() + " " + student.getLastName());
                response.setClasse(student.getClasse());
                response.setStudentId(student.getIdUser());
            }

            response.setInternshipState(internship.getInternshipState().name());
            response.setTypeInternship(internship.getPost().getTypeInternship().name());
            response.setTitle(internship.getPost().getTitle());
            response.setContent(internship.getPost().getContent());
            response.setCompanyName(internship.getPost().getCompany().getName());

            if (internship.getPost().getSkills() != null) {
                response.setSkills(new ArrayList<>(internship.getPost().getSkills()));
            } else {
                response.setSkills(new ArrayList<>());
            }

            response.setIsValidator(
                    internship.getValidator() != null &&
                            internship.getValidator().getIdUser().equals(idUser)
            );

            return response;
        }).collect(Collectors.toList());
    }



    @Override
    public Map<String, Object> getInternshipStatistics() {
        return null;
    }


}



