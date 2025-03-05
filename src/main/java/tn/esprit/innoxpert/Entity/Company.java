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
    
        Long phone;
        LocalDate foundingYear;
        LocalDate LabelDate;
        String website;
        String founders;
    
        @Column(nullable = false)
        String secretKey; // Clé secrète pour la génération du mot de passe

        // ✅ Suppression en cascade des posts, mais pas de mise à jour forcée
        @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonIgnore
        List<Post> posts;
    
        // ✅ Suppression en cascade de l'owner mais éviter la mise à jour accidentelle
        @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
        @JoinColumn(name = "idUser", referencedColumnName = "idUser")

        User owner;
    
        // ✅ Suppression en cascade des followers mais éviter la mise à jour accidentelle
        @ManyToMany(mappedBy = "followedCompanies", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})

        private List<User> followers;
    }