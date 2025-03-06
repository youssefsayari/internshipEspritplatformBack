package tn.esprit.innoxpert.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Document name cannot be null.")
    @Size(min = 5, max = 100, message = "Document name must be between 5 and 100 characters.")
    String name;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Document type is required.")
    TypeDocument typeDocument;

    @NotNull(message = "You should upload a PDF file.")
    String fileName;  // Stores the file name


    @NotNull(message = "You should upload a PDF file.")
    String filePath;  // Stores the file location

    boolean isDownloadable = false;

    @ManyToOne
    @JsonIgnore
    User student;
}
