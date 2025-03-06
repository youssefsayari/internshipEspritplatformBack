package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Service.DocumentServiceInterface;
import tn.esprit.innoxpert.Service.UserServiceInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Document Management")
@RestController
@AllArgsConstructor
@RequestMapping("/documents")
public class DocumentRestController {

    private final DocumentServiceInterface documentService;
    private final UserServiceInterface userService;
    private static final String DOCUMENTS_DIR = "src/main/resources/documents/";

    private static final List<String> ALLOWED_FILES = List.of(
            "lettre_affectation.pdf",
            "demande_de_stage.pdf",
            "journal.pdf",
            "convention_de_stage.pdf"
    );

    // ✅ Get All Documents
    @GetMapping("/getAllDocuments")
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    // ✅ Get Document by ID
    @GetMapping("/getDocumentById/{idDocument}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long idDocument) {
        try {
            return ResponseEntity.ok(documentService.getDocumentById(idDocument));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addDocument")
    public ResponseEntity<?> addDocument(
            @RequestParam("name") String name,
            @RequestParam("typeDocument") String typeDocument,
            @RequestPart("file") MultipartFile file) {

        try {
            // Validate input before passing to the service
            if (name == null || name.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Document name is required"));
            }
            if (typeDocument == null || typeDocument.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Document type is required"));
            }

            // Call the service method to add the document
            Document savedDocument = documentService.addDocument(name, typeDocument, file);
            return ResponseEntity.ok(savedDocument);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "File upload failed: " + e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An unexpected error occurred"));
        }
    }



    // ✅ Upload Document by ID
    @GetMapping("/uploadDocument/{id}")
    public ResponseEntity<byte[]> uploadDocument(@PathVariable Long id) {
        try {
            byte[] fileData = documentService.getDocumentFile(id);
            Document document = documentService.getDocumentById(id);
            String fileName = document.getFileName();
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

    // ✅ Delete Document
    @DeleteMapping("/deleteDocument/{idDocument}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable Long idDocument) {
        try {
            documentService.removeDocumentById(idDocument);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Update Document
    @PutMapping("/updateDocument")
    public ResponseEntity<Document> updateDocument(@RequestBody Document document) {
        try {
            Document updatedDocument = documentService.updateDocument(document);
            return ResponseEntity.ok(updatedDocument);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ Download Document by ID
    @GetMapping("/downloadDocument/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        try {
            return documentService.downloadDocument(id);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Download Predefined Documents
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadPredefinedDocument(@PathVariable String fileName) {
        try {
            // Ensure only predefined files can be downloaded
            if (!ALLOWED_FILES.contains(fileName)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Path filePath = Paths.get(DOCUMENTS_DIR + fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                System.err.println("File not found: " + filePath);  // Log file path for debugging
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // ✅ upload Predefined Documents
    @PostMapping("/uploadDocuments")
    public ResponseEntity<Map<String, String>> uploadDocuments(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("types") List<String> types) {
        Map<String, String> response = new HashMap<>();
        try {
            if (files.size() != types.size()) {
                response.put("message", "Mismatch between the number of files and types.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            for (int i = 0; i < files.size(); i++) {
                documentService.saveDocument("Document_" + (i + 1), types.get(i), files.get(i));
            }

            response.put("message", "Documents uploaded successfully!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    // ✅ Generate and Download Student CV
    @GetMapping("/generateStudentCV/{userId}")
    public ResponseEntity<byte[]> generateStudentCV(@PathVariable Long userId) {
        try {
            byte[] pdfBytes = documentService.generateStudentCV(userId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CV_" + userId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Download Generated Student CV
    @GetMapping("/downloadStudentCV/{userId}")
    public ResponseEntity<byte[]> downloadStudentCV(@PathVariable Long userId) {
        try {
            return documentService.downloadCV(userId);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}
