package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name; // Document title
    @Enumerated(EnumType.STRING)
    TypeDocument typeDocument;

    String fileName;  // Stores the file name
    String filePath;  // Stores the file location

    boolean isDownloadable = false;

    @ManyToOne
    User student;
}
