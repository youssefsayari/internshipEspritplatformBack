package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)


@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"idUser", "post_id"}),
        @UniqueConstraint(columnNames = {"idUser", "comment_id"})
})


public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int stars;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    @JsonIgnore

    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = true)
    @JsonIgnore

    Comment comment;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false) // Ajout d'une relation avec User
    @JsonIgnore

    private User user;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt = LocalDateTime.now(); // Ajout de la date de création


}
