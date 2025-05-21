package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.innoxpert.Entity.Comment;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.Rating;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String imageUrl;
    private String imageId;

    public Image(String name, String imageUrl, String imageId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.imageId = imageId;
    }

}
