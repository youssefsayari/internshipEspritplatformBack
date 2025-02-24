package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
