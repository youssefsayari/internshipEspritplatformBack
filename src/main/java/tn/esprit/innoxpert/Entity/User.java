package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long idUser;

     String identifiant;
     String password;
     String email;
     Long telephone;

    @Enumerated(EnumType.STRING)
    TypeUser typeUser;

    @OneToOne

    UserDetails userDetails;

    @OneToMany (cascade = CascadeType.ALL, mappedBy="student")
    List<Task> tasks;







}
