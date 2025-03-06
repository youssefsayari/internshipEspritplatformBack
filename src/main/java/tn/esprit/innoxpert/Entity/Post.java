package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 255, message = "Le titre doit contenir entre 3 et 255 caractères")
    private String title;

    @NotBlank(message = "Le contenu est obligatoire")
    @Size(min = 10, max = 255, message = "Le content doit contenir entre 10 et 255 caractères")
    @Column(columnDefinition = "TEXT")
    private String content;


    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    TypeInternship typeInternship;

    @Column(nullable = true) // Peut être NULL si pas d'expiration
    LocalDateTime expiryDateTime;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    Company company;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Internship> internships = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "Post_skills",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "skills_id"))
    @JsonIgnore
    private List<Skill> skills = new ArrayList<>();

}