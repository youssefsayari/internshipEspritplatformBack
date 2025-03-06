package tn.esprit.innoxpert.Service;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.DocumentRepository;
import tn.esprit.innoxpert.Repository.UserRepository;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class DocumentService implements DocumentServiceInterface {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
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
        String filePath = UPLOAD_DIR + File.separator + uniqueFileName;

        // Save the file to disk
        file.transferTo(new File(filePath));

        // Save document details in the database
        Document document = new Document();
        document.setName(name);
        document.setTypeDocument(TypeDocument.valueOf(typeDocument));  // Enum value conversion
        document.setFileName(uniqueFileName);
        document.setFilePath(filePath);

        return documentRepository.save(document);
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
    public void saveDocument(String name, String typeDocument, MultipartFile file) throws IOException {
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

        // Ensure the typeDocument is valid
        TypeDocument type = TypeDocument.valueOf(typeDocument);

        // Retrieve the static user (student with id=1)
        User student = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));

        // Check if the student has already uploaded a document of the same type
        boolean alreadyUploaded = student.getDocuments().stream()
                .anyMatch(doc -> doc.getTypeDocument().equals(type));

        if (alreadyUploaded) {
            throw new IllegalArgumentException("You have already uploaded a document of type: " + type);
        }

        // Save document details in the database
        Document document = new Document();
        document.setName(name);
        document.setTypeDocument(type);
        document.setFileName(uniqueFileName);
        document.setFilePath(filePath);
        document.setStudent(student); // Associate with user

        documentRepository.save(document);
    }


    @Override
    public byte[] generateStudentCV(Long userId) throws IOException {
        // Fetch user and associated data
        User student = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserInfo userInfo = student.getUserInfo();
        List<Expertise> expertiseList = (userInfo != null) ? userInfo.getExpertises() : List.of();

        // Static experiences and languages (hardcoded)
        List<String> experienceList = List.of(
                "Software Developer Intern at ABC Technologies (June 2022 - August 2022)\n   Worked on developing and maintaining web applications.",
                "Junior Web Developer at XYZ Solutions (September 2022 - Present)\n   Building responsive websites using HTML, CSS, and JavaScript."
        );

        List<String> languageList = List.of(
                "English - Fluent",
                "French - Intermediate",
                "Arabic - Native"
        );

        // PDF generation
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument);

        // Header - CV Title
        document.add(new Paragraph("Curriculum Vitae")
                .setFontSize(36)
                .setBold()
                .setFontColor(new DeviceRgb(0, 102, 204))
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("\n"));

        // Full Name Styling
        document.add(new Paragraph(student.getFirstName() + " " + student.getLastName())
                .setFontSize(22)
                .setBold()
                .setFontColor(new DeviceRgb(0, 0, 0))
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("\n"));

        // Professional Summary
        document.add(new Paragraph("Professional Summary")
                .setFontSize(20)
                .setBold()
                .setFontColor(new DeviceRgb(50, 50, 150))
                .setBackgroundColor(new DeviceRgb(240, 240, 240)));
        document.add(new Paragraph("A highly motivated and results-driven individual with a passion for software development, seeking a position to utilize my skills in web development and software engineering."));
        document.add(new Paragraph("\n"));

        // Expertise Section
        document.add(new Paragraph("Core Competencies")
                .setFontSize(18)
                .setBold()
                .setFontColor(new DeviceRgb(50, 50, 150))
                .setBackgroundColor(new DeviceRgb(240, 240, 240)));
        if (!expertiseList.isEmpty()) {
            for (Expertise expertise : expertiseList) {
                document.add(new Paragraph("✔ " + expertise.getTypeExpertise().name())
                        .setFontSize(12)
                        .setFontColor(new DeviceRgb(0, 0, 0)));
            }
        } else {
            document.add(new Paragraph("No expertise listed."));
        }
        document.add(new Paragraph("\n"));

        // Experience Section
        document.add(new Paragraph("Experience")
                .setFontSize(20)
                .setBold()
                .setFontColor(new DeviceRgb(50, 50, 150))
                .setBackgroundColor(new DeviceRgb(240, 240, 240)));
        if (!experienceList.isEmpty()) {
            for (String experience : experienceList) {
                document.add(new Paragraph("• " + experience)
                        .setFontSize(12)
                        .setFontColor(new DeviceRgb(0, 0, 0)));
                document.add(new Paragraph("\n"));
            }
        } else {
            document.add(new Paragraph("No experience listed."));
        }

        // Languages Section
        document.add(new Paragraph("Languages")
                .setFontSize(20)
                .setBold()
                .setFontColor(new DeviceRgb(50, 50, 150))
                .setBackgroundColor(new DeviceRgb(240, 240, 240)));
        if (!languageList.isEmpty()) {
            for (String language : languageList) {
                document.add(new Paragraph("• " + language)
                        .setFontSize(12)
                        .setFontColor(new DeviceRgb(0, 0, 0)));
            }
        } else {
            document.add(new Paragraph("No languages listed."));
        }
        document.add(new Paragraph("\n"));

        // Education Section
        document.add(new Paragraph("Education")
                .setFontSize(20)
                .setBold()
                .setFontColor(new DeviceRgb(50, 50, 150))
                .setBackgroundColor(new DeviceRgb(240, 240, 240)));
        document.add(new Paragraph("Bachelor of Science in Computer Science, ESPRIT, 2020 - 2023"));
        document.add(new Paragraph("\n"));

        // Certifications Section
        document.add(new Paragraph("Certifications")
                .setFontSize(20)
                .setBold()
                .setFontColor(new DeviceRgb(50, 50, 150))
                .setBackgroundColor(new DeviceRgb(240, 240, 240)));
        document.add(new Paragraph("Certified Java Developer - Oracle"));
        document.add(new Paragraph("Web Development Bootcamp - XYZ Academy"));
        document.add(new Paragraph("\n"));

        // Contact Info with social media links (below other content)
        String fullName = student.getFirstName() + " " + student.getLastName();
        document.add(new Paragraph("Contact Info:")
                .setFontSize(18)
                .setBold()
                .setFontColor(new DeviceRgb(50, 50, 150))
                .setBackgroundColor(new DeviceRgb(240, 240, 240)));

        // Email and Phone
        document.add(new Paragraph("📧 " + student.getEmail())
                .setFontSize(14)
                .setFontColor(new DeviceRgb(0, 0, 0)));
        document.add(new Paragraph("📱 " + student.getTelephone().toString())
                .setFontSize(14)
                .setFontColor(new DeviceRgb(0, 0, 0)));

        // Facebook and LinkedIn Links
        document.add(new Paragraph("🔗 Facebook: " + fullName)
                .setFontSize(14)
                .setFontColor(new DeviceRgb(59, 89, 152)));  // Facebook blue
        document.add(new Paragraph("🔗 LinkedIn: " + fullName)
                .setFontSize(14)
                .setFontColor(new DeviceRgb(0, 119, 181)));  // LinkedIn blue
        document.add(new Paragraph("\n"));

        // Footer (optional, for additional contact or links)
        document.add(new Paragraph("Contact: " + student.getEmail())
                .setFontSize(10)
                .setItalic()
                .setFontColor(new DeviceRgb(50, 50, 150)));

        // Finalize document
        document.close();
        return byteArrayOutputStream.toByteArray();
    }
    

    @Override
    public ResponseEntity<byte[]> downloadCV(Long userId) throws IOException {
        byte[] pdfBytes = generateStudentCV(userId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CV.pdf")
                .body(pdfBytes);
    }
}




