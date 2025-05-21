package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Internship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Le titre ne peut pas être vide.")
    @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères.")
    String title;

    @NotBlank(message = "La description ne peut pas être vide.")
    @Size(min = 10, max = 500, message = "La description doit contenir entre 10 et 500 caractères.")
    String description;

    @NotNull(message = "L'ID du document est obligatoire.")
    Long id_document;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "L'état du stage est obligatoire.")
    InternshipState internshipState;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "Internship_users",
            joinColumns = @JoinColumn(name = "internship_id"),
            inverseJoinColumns = @JoinColumn(name = "users_idUser"))
    private List<User> users = new ArrayList<>();


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "validator_id")
    private User validator;


}
