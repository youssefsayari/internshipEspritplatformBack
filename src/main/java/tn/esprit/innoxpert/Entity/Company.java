package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    String secretKey;


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Post> posts;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "idUser")
    User owner;


    @ManyToMany(mappedBy = "followedCompanies")
    private List<User> followers; // Liste des utilisateurs qui suivent cette entreprise



}