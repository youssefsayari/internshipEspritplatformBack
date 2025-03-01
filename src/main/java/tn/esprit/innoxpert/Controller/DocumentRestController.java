package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Service.DocumentServiceInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Tag(name = "Document Management")
@RestController
@AllArgsConstructor
@RequestMapping("/documents")
public class DocumentRestController {

    private final DocumentServiceInterface documentService;

    @GetMapping("/getAllDocuments")
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/getDocumentById/{idDocument}")
    public Document getDocumentById(@PathVariable("idDocument") Long idDocument) {
        return documentService.getDocumentById(idDocument);
    }

    @PostMapping("/addDocument")
    public ResponseEntity<Document> addDocument(
            @RequestParam("name") String name,
            @RequestParam("typeDocument") String typeDocument,
            @RequestParam("file") MultipartFile file) {
        try {
            Document savedDocument = documentService.addDocument(name, typeDocument, file);
            return ResponseEntity.ok(savedDocument);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Return a 400 Bad Request if file is empty
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/uploadDocument/{id}")
    public ResponseEntity<byte[]> uploadDocument(@PathVariable Long id) {
        try {
            byte[] fileData = documentService.getDocumentFile(id);

            // Get the document from the database to retrieve its original filename
            Document document = documentService.getDocumentById(id);
            String fileName = document.getFileName();

            // Detect file type dynamically
            Path path = Paths.get(document.getFilePath());
            String mimeType = Files.probeContentType(path);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
                    .contentType(MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream"))
                    .body(fileData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteDocument/{idDocument}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable("idDocument") Long idDocument) {
        try {
            documentService.removeDocumentById(idDocument);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateDocument")
    public ResponseEntity<Document> updateDocument(@RequestBody Document document) {
        try {
            Document updatedDocument = documentService.updateDocument(document);
            return ResponseEntity.ok(updatedDocument);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/downloadDocument/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable("id") Long id) {
        try {
            return documentService.downloadDocument(id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
