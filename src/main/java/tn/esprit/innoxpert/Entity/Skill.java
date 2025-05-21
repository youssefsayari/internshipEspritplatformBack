package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<User> users = new ArrayList<>();

}
