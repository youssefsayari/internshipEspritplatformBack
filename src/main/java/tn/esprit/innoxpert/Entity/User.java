package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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

     Long OTP;


    @Enumerated(EnumType.STRING)
    TypeUser typeUser;

    @OneToOne
    UserInfo userInfo;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Meeting> meetingsAsParticipant;

    @OneToMany(mappedBy = "organiser", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Meeting> meetingsAsOrganiser;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
    @JsonIgnore
    List<Task> tasks;
    /*----------------start l5edmet amin--------------------*/

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Document> documents = new ArrayList<>();

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Defense defense;


    @ManyToMany(mappedBy = "tutors", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JsonIgnore  // Prevent infinite loop
    Set<Defense> defenses;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    @JsonIgnore
    private Document document;

    /*----------------start l5edmet sayari--------------------*/
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_followed_companies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    @JsonIgnoreProperties({"followers", "owner"}) // Ajoutez cette annotation
    private List<Company> followedCompanies = new ArrayList<>();




    /*----------------end l5edmet sayari--------------------*/



    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Internship> internships = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "User_skills",
            joinColumns = @JoinColumn(name = "user_idUser"),
            inverseJoinColumns = @JoinColumn(name = "skills_id"))
    @JsonIgnore
    private List<Skill> skills = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    @JsonIgnore
    private User tutor;

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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}
