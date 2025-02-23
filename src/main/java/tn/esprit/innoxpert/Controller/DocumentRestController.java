package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Service.DocumentServiceInterface;

import java.util.List;

@Tag(name = "Document Management")
@RestController
@AllArgsConstructor
@RequestMapping("/documents")
//@CrossOrigin(origins = "http://localhost:4200") // Allow Angular requests
public class DocumentRestController {

    private final DocumentServiceInterface documentServiceInterface;

    // Get all documents
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentServiceInterface.getAllDocuments());
    }

    // Get document by ID
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(documentServiceInterface.getDocumentById(id));
    }

    // Add a new document
    @PostMapping
    public ResponseEntity<Document> addDocument(@RequestBody Document document) {
        return ResponseEntity.ok(documentServiceInterface.addDocument(document));
    }

    // Update an existing document
    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document document) {
        document.setId(id); // Ensure the correct ID is set
        return ResponseEntity.ok(documentServiceInterface.updateDocument(document));
    }

    // Delete a document by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentServiceInterface.removeDocumentById(id);
        return ResponseEntity.noContent().build();
    }
}
