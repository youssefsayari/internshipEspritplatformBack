package tn.esprit.innoxpert.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Entity.TimeLine;
import tn.esprit.innoxpert.Entity.TypeDocument;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Repository.DocumentRepository;
import tn.esprit.innoxpert.Repository.TimeLineRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimeDocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final TimeLineRepository timeLineRepository;

    private final String uploadDir = "fichierUpload";

    public Document saveDocument(MultipartFile file, TypeDocument typeDocument, Long studentId, String nomEtape) throws IOException {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        String cleanedOriginalName = originalFileName != null ? originalFileName.replaceAll(" ", "_") : "document.pdf";

        String userFullName = student.getFirstName() + "_" + student.getLastName();
        userFullName = userFullName.replaceAll(" ", "_");

        String finalFileName = userFullName + "_" + cleanedOriginalName;

        Path filePath = Paths.get(uploadDir, finalFileName);
        Files.write(filePath, file.getBytes());

        Document document = new Document();
        document.setName(originalFileName);
        document.setTypeDocument(typeDocument);
        document.setFileName(finalFileName);
        document.setFilePath(filePath.toString());
        document.setStudent(student);
        document.setDownloadable(true);

        Document savedDocument = documentRepository.save(document);

        TimeLine timeline = timeLineRepository.findByTitleAndStudent_IdUser(nomEtape, studentId)
                .orElseThrow(() -> new RuntimeException("Timeline step not found for: " + nomEtape));

        timeline.setDocument(savedDocument);
        timeLineRepository.save(timeline);

        return savedDocument;
    }


    public ResponseEntity<Resource> downloadDocument(Long documentId) throws IOException {
        Document doc = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        Path path = Paths.get(doc.getFilePath());
        Resource resource = new UrlResource(path.toUri());
        System.out.println("Nom du fichier envoyé : " + doc.getFileName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                .body(resource);
    }
}
