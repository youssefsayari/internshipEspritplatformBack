package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @NotNull(message = "Le nom est obligatoire")
    @Size(min = 3, message = "Le nom doit contenir au moins 3 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Le nom ne doit contenir que des lettres, chiffres et espaces")
    String name;

    @NotNull(message = "L'abréviation est obligatoire")
    String abbreviation;

    @NotNull(message = "L'adresse est obligatoire")
    @Size(min = 5, message = "L'adresse doit contenir au moins 5 caractères")
    String address;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le secteur est obligatoire")
    TypeSector sector;

    @Column(unique = true, nullable = false)
    @NotNull(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    String email;

    @NotNull(message = "Le numéro de téléphone est obligatoire")
    @Min(value = 10000000, message = "Le téléphone doit contenir au moins 8 chiffres")
    @Max(value = 999999999999999L, message = "Le téléphone ne doit pas dépasser 15 chiffres")
    Long phone;


    @NotNull(message = "L'année de fondation est obligatoire")
    Date foundingYear;

    @NotNull(message = "La date du label est obligatoire")
    Date labelDate;

    @NotNull(message = "Le site web est obligatoire")
    @Pattern(regexp = "https?://.+", message = "Le site web doit être une URL valide commençant par http:// ou https://")
    String website;

    @NotNull(message = "Le(s) fondateur(s) sont obligatoires")
    @Size(min = 1, message = "Il doit y avoir au moins un fondateur")
    String founders;

    @Column(nullable = false)
    @NotNull(message = "La clé secrète est obligatoire")
    @Size(min = 8, message = "La clé secrète doit contenir au moins 8 caractères")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9]).{8,}$",
            message = "La clé secrète doit contenir au moins une majuscule, un chiffre et un caractère spécial")
    String secretKey;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE) // Ajouter cette annotation
    List<Post> posts;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "idUser", referencedColumnName = "idUser")
    User owner;


    @ManyToMany(mappedBy = "followedCompanies", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore
    private List<User> followers;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    // Ajout de la relation avec Image
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @NotNull(message = "Le nombre d'employés est obligatoire")
    @Min(value = 1, message = "L'entreprise doit avoir au moins 1 employé")
    @Max(value = 1000000, message = "Le nombre d'employés ne peut excéder 1 000 000")
    Integer numEmployees;

}