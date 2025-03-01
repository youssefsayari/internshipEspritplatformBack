package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.DocumentRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DocumentService implements DocumentServiceInterface {
    private final DocumentRepository documentRepository;
    private static final String UPLOAD_DIR = "C:/uploads/";  // Ensure this directory exists

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document with ID " + documentId + " not found."));
    }

    @Override
    public Document addDocument(String name, String typeDocument, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty. Please upload a valid document.");
        }

        // Ensure the upload directory exists
        File uploadFolder = new File(UPLOAD_DIR);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();  // Create directory if it doesn't exist
        }

        // Create a unique filename to prevent conflicts
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;
        String filePath = UPLOAD_DIR + uniqueFileName;

        // Save the file to disk
        file.transferTo(new File(filePath));

        // Save document details in the database
        Document document = new Document();
        document.setName(name);
        document.setTypeDocument(tn.esprit.innoxpert.Entity.TypeDocument.valueOf(typeDocument));
        document.setFileName(uniqueFileName);
        document.setFilePath(filePath);

        return documentRepository.save(document);
    }

    @Override
    public byte[] getDocumentFile(Long documentId) throws IOException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document with ID " + documentId + " not found."));

        String filePath = document.getFilePath();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new NotFoundException("File not found at path: " + filePath);
        }

        return Files.readAllBytes(file.toPath());
    }

    @Override
    public ResponseEntity<byte[]> downloadDocument(Long documentId) throws IOException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document with ID " + documentId + " not found."));

        Path path = Paths.get(document.getFilePath());
        byte[] fileData = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.getFileName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileData);
    }

    @Override
    public void removeDocumentById(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new NotFoundException("Document with ID " + documentId + " was not found.");
        }

        // Find the document and delete the associated file from storage
        Document document = documentRepository.findById(documentId).orElseThrow(() -> new NotFoundException("Document not found"));
        File file = new File(document.getFilePath());
        if (file.exists()) {
            file.delete(); // Delete the physical file
        }

        documentRepository.deleteById(documentId);
    }

    @Override
    public Document updateDocument(Document d) {
        if (!documentRepository.existsById(d.getId())) {
            throw new NotFoundException("Document with ID: " + d.getId() + " was not found. Cannot update.");
        }
        return documentRepository.save(d);
    }
}
