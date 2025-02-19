package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

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
    String name;

    String abbreviation;

    String address;

    @Enumerated(EnumType.STRING)
    TypeSector sector;

    @Column(unique = true, nullable = false)
    String email;

    String phone;

    LocalDate foundingYear ;

    LocalDate LabelDate ;

    String website ;

    String founders ;

    @Column(nullable = false)
    String secretKey; // Clé secrète pour la génération du mot de passe


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Post> posts;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "idUser") // Clé étrangère pour l'utilisateur
    User owner;





}
