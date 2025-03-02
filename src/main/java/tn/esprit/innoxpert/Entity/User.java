package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long idUser;
     String firstName;
     String lastName;
     String identifiant;
     String password;
     String email;
     Long telephone;
     String classe;
     String quiz;

    @Enumerated(EnumType.STRING)
    TypeUser typeUser;

    @OneToOne
    UserInfo userInfo;

    @OneToMany (cascade = CascadeType.ALL, mappedBy="student")
    List<Task> tasks;


    @ManyToMany
    @JoinTable(
            name = "user_followed_companies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private List<Company> followedCompanies = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private List<Internship> internships = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "User_skills",
            joinColumns = @JoinColumn(name = "user_idUser"),
            inverseJoinColumns = @JoinColumn(name = "skills_id"))
    @JsonIgnore
    private List<Skill> skills = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return identifiant;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
