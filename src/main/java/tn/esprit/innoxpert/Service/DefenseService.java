package tn.esprit.innoxpert.Service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.DefenseRequest;
import tn.esprit.innoxpert.DTO.DefenseWithEvaluationsDTO;
import tn.esprit.innoxpert.DTO.TutorEvaluationDTO;
import tn.esprit.innoxpert.DTO.UserDTO;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.TypeDocument;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.DefenseRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DefenseService implements DefenseServiceInterface {

    private final DefenseRepository defenseRepository;
    private final UserRepository userRepository;
    private final DefenseConflictChecker conflictChecker;

    @Override
    public List<Defense> getAllDefenses() {
        return defenseRepository.findAllWithStudentsAndTutors();
    }

    @Override
    public Defense getDfenseById(Long idDefense) {
        return defenseRepository.findById(idDefense)
                .orElseThrow(() -> new NotFoundException("Defense with ID: " + idDefense + " was not found."));
    }

    @Override
    public Defense addDefense(Long studentId, DefenseRequest defenseRequest) {
        // 1. Validate the student
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with ID " + studentId + " not found"));

        if (student.getTypeUser() != TypeUser.Student) {
            throw new IllegalArgumentException("User with ID " + studentId + " is not a student");
        }

        // 2. Check if the student already has a defense
        Optional<Defense> existingDefense = defenseRepository.findDefenseByStudentId(studentId);
        if (existingDefense.isPresent()) {
            throw new IllegalArgumentException("This student already has a scheduled defense.");
        }

        // 3. Validate the tutors (exactly 3 tutors)
        Set<Long> tutorIds = defenseRequest.getTutorIds();
        if (tutorIds == null || tutorIds.size() != 3) {
            throw new IllegalArgumentException("Exactly 3 tutor IDs must be specified");
        }

        List<User> tutors = userRepository.findAllById(tutorIds);
        if (tutors.size() != 3) {
            throw new IllegalArgumentException("One or more tutors not found");
        }

        for (User tutor : tutors) {
            if (tutor.getTypeUser() != TypeUser.Tutor) {
                throw new IllegalArgumentException("User with ID " + tutor.getIdUser() + " is not a tutor");
            }
        }

        // 4. Check for scheduling conflicts
        conflictChecker.checkDefenseConflicts(
                defenseRequest.getClassroom(),
                defenseRequest.getDefenseDate(),
                defenseRequest.getDefenseTime(),
                new HashSet<>(tutors)
        );

        // 5. Validate date and time constraints
        if (defenseRequest.getDefenseDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Defense date must be today or in the future.");
        }

        if (defenseRequest.getDefenseTime().isBefore(LocalTime.parse("08:00")) ||
                defenseRequest.getDefenseTime().isAfter(LocalTime.parse("18:00"))) {
            throw new IllegalArgumentException("Defense time must be between 08:00 and 18:00.");
        }

        // 6. Create and save the defense
        Defense defense = new Defense();
        defense.setStudent(student);
        defense.setDefenseDate(defenseRequest.getDefenseDate());
        defense.setDefenseTime(defenseRequest.getDefenseTime());
        defense.setClassroom(defenseRequest.getClassroom());
        defense.setReportSubmitted(defenseRequest.isReportSubmitted());
        defense.setInternshipCompleted(defenseRequest.isInternshipCompleted());
        defense.setTutors(new HashSet<>(tutors));

        // 7. Check if defense degree is null and set it to 0
        if (defense.getDefenseDegree() == null) {
            defense.setDefenseDegree(0.0); // Set to 0 if null
        }

        // Save and return the defense
        return defenseRepository.save(defense);
    }



    @Override
    @Transactional
    public void removeDefenseById(Long idDefense) {
        Defense defense = defenseRepository.findById(idDefense)
                .orElseThrow(() -> new NotFoundException("Defense with ID: " + idDefense + " was not found."));

        for (User tutor : defense.getTutors()) {
            tutor.getDefenses().remove(defense);
        }

        defense.getTutors().clear();
        defenseRepository.save(defense);
        defenseRepository.delete(defense);
    }

    @Override
    @Transactional
    public Defense updateDefense(Long defenseId, DefenseRequest request) {
        Defense defense = defenseRepository.findById(defenseId)
                .orElseThrow(() -> new NotFoundException("Defense with ID: " + defenseId + " was not found."));

        // Prevent update if degree is not 0.0 (i.e., already evaluated)
        if (defense.getDefenseDegree() != 0.0) {
            throw new IllegalStateException("This defense has already been evaluated and cannot be updated.");
        }


        // Validate date
        if (defense.getDefenseDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Defense date must be today or in the future.");
        }

        // Validate time
        if (defense.getDefenseTime().isBefore(LocalTime.parse("08:00")) ||
                defense.getDefenseTime().isAfter(LocalTime.parse("18:00"))) {
            throw new IllegalArgumentException("Defense time must be between 08:00 and 18:00.");
        }

        // Apply new values
        defense.setDefenseDate(request.getDefenseDate());
        defense.setDefenseTime(request.getDefenseTime());
        defense.setClassroom(request.getClassroom());

        // Preserve original data
        defense.setTutors(defense.getTutors());
        defense.setStudent(defense.getStudent());
        defense.setDefenseDegree(defense.getDefenseDegree());

        return defenseRepository.save(defense);
    }


    @Override
    public boolean isDefenseSlotAvailable(String classroom, LocalDate date, LocalTime time) {
        return conflictChecker.isClassroomAvailable(classroom, date, time);
    }
    @Override
    public List<Defense> getDefensesByTutorId(Long tutorId) {
        // First verify the tutor exists
        User tutor = userRepository.findById(tutorId)
                .orElseThrow(() -> new NotFoundException("Tutor with ID: " + tutorId + " was not found."));

        // Verify it's actually a tutor
        if (tutor.getTypeUser() != TypeUser.Tutor) {
            throw new IllegalArgumentException("User with ID " + tutorId + " is not a tutor");
        }

        return defenseRepository.findDefensesByTutorId(tutorId);
    }

    public List<DefenseWithEvaluationsDTO> getDefensesWithEvaluationsByTutor(Long tutorId) {
        List<Defense> defenses = defenseRepository.findDefensesByTutorId(tutorId);

        return defenses.stream().map(defense -> {
            DefenseWithEvaluationsDTO dto = new DefenseWithEvaluationsDTO();
            dto.setIdDefense(defense.getIdDefense());
            dto.setClassroom(defense.getClassroom());
            dto.setDefenseDate(defense.getDefenseDate());
            dto.setDefenseTime(defense.getDefenseTime());
            dto.setReportSubmitted(defense.isReportSubmitted());
            dto.setInternshipCompleted(defense.isInternshipCompleted());
            dto.setDefenseDegree(defense.getDefenseDegree());

            // Convert student
            User student = defense.getStudent();
            UserDTO studentDTO = new UserDTO();
            studentDTO.setIdUser(student.getIdUser());
            studentDTO.setFirstName(student.getFirstName());
            studentDTO.setLastName(student.getLastName());
            dto.setStudent(studentDTO);

            List<Long> tutorIds = defense.getTutors().stream()
                    .map(User::getIdUser)
                    .collect(Collectors.toList());
            dto.setTutors(tutorIds);

            // Convert evaluations
            List<TutorEvaluationDTO> evalDTOs = defense.getEvaluations().stream().map(eval -> {
                TutorEvaluationDTO evalDTO = new TutorEvaluationDTO();
                evalDTO.setId(eval.getId());
                evalDTO.setTutorId(eval.getTutor().getIdUser());
                evalDTO.setGrade(eval.getGrade());
                evalDTO.setRemarks(eval.getRemarks());
                evalDTO.setStatus(eval.getStatus().name());
                return evalDTO;
            }).collect(Collectors.toList());

            dto.setEvaluations(evalDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Defense> getDefensesByStudentId(Long studentId) {
        return defenseRepository.findDefensesByStudentId(studentId);
    }

    public Map<String, List<Defense>> getDefenseStats(List<Defense> defenses) {
        // Initialize the categories
        List<Defense> excellentStudents = new ArrayList<>();
        List<Defense> averageStudents = new ArrayList<>();
        List<Defense> badStudents = new ArrayList<>();
        List<Defense> notEvaluated = new ArrayList<>();

        // Classify each defense based on the defense degree
        for (Defense defense : defenses) {
            Double defenseDegree = defense.getDefenseDegree(); // Assuming it's a Double, which can be null

            if (defenseDegree == null || defenseDegree == 0.0) {
                notEvaluated.add(defense); // Add to "Not Evaluated" if defenseDegree is null or 0
            } else if (defenseDegree >= 15) {
                excellentStudents.add(defense);
            } else if (defenseDegree >= 10) {
                averageStudents.add(defense);
            } else if (defenseDegree > 0) { // Degree > 0 is bad but not null or 0
                badStudents.add(defense);
            }
        }

        // Prepare the result map with categorized students
        Map<String, List<Defense>> result = new HashMap<>();
        result.put("Excellent", excellentStudents);
        result.put("Average", averageStudents);
        result.put("Bad", badStudents);
        result.put("Not Evaluated", notEvaluated); // Add "Not Evaluated" category

        return result;
    }

    private void addHeader(Document document, Defense defense, User student) throws IOException {
        // Title
        Paragraph title = new Paragraph("DEFENSE EVALUATION GRID")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(0, 82, 155));
        document.add(title);

        // Subtitle
        Paragraph subtitle = new Paragraph("Project Defense Assessment Form")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(100, 100, 100));
        document.add(subtitle);

        document.add(new Paragraph("\n"));

        // Student info table
        float[] columnWidths = {2, 5};
        Table infoTable = new Table(UnitValue.createPercentArray(columnWidths));

        // Student name
        infoTable.addCell(createInfoCell("Student:", true));
        infoTable.addCell(createInfoCell(student.getFirstName() + " " + student.getLastName(), false));

        // Defense date
        infoTable.addCell(createInfoCell("Defense Date:", true));
        infoTable.addCell(createInfoCell(
                defense.getDefenseDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                false));

        // Defense time
        infoTable.addCell(createInfoCell("Defense Time:", true));
        infoTable.addCell(createInfoCell(defense.getDefenseTime().toString(), false));

        // Classroom
        infoTable.addCell(createInfoCell("Classroom:", true));
        infoTable.addCell(createInfoCell(defense.getClassroom(), false));

        document.add(infoTable);
        document.add(new Paragraph("\n"));
    }

    private Cell createInfoCell(String text, boolean isHeader) {
        Cell cell = new Cell().add(new Paragraph(text));
        if (isHeader) {
            cell.setBackgroundColor(new DeviceRgb(240, 240, 240))
                    .setBold();
        }
        return cell;
    }

    private void addEvaluationTable(Document document) {
        // Evaluation criteria table
        float[] evalColumnWidths = {5, 2, 2, 3};
        Table evalTable = new Table(UnitValue.createPercentArray(evalColumnWidths));

        // Table header
        evalTable.addHeaderCell(createHeaderCell("Evaluation Criteria"));
        evalTable.addHeaderCell(createHeaderCell("Max Points"));
        evalTable.addHeaderCell(createHeaderCell("Awarded"));
        evalTable.addHeaderCell(createHeaderCell("Comments"));

        // Evaluation items
        addEvaluationItem(evalTable, "1. Oral Presentation", 4,
                "Clarity, structure, time management, engagement");
        addEvaluationItem(evalTable, "2. Technical Content", 6,
                "Depth, accuracy, relevance, innovation");
        addEvaluationItem(evalTable, "3. Methodology", 4,
                "Appropriateness, rigor, documentation");
        addEvaluationItem(evalTable, "4. Results & Analysis", 4,
                "Quality, interpretation, validation");
        addEvaluationItem(evalTable, "5. Professionalism", 2,
                "Dress code, behavior, response to questions");

        // Total row
        Cell criteriaCell = new Cell(1, 1).add(new Paragraph("TOTAL").setBold());
        Cell maxPointsCell = new Cell(1, 1).add(new Paragraph("20").setBold());
        Cell awardedCell = new Cell(1, 1).add(new Paragraph("").setBold());
        Cell commentsCell = new Cell(1, 1).add(new Paragraph("").setBold());

        evalTable.addCell(criteriaCell);
        evalTable.addCell(maxPointsCell);
        evalTable.addCell(awardedCell);
        evalTable.addCell(commentsCell);

        document.add(evalTable);
        document.add(new Paragraph("\n"));
    }

    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(new DeviceRgb(0, 82, 155))
                .setFontColor(ColorConstants.WHITE);
    }

    private void addEvaluationItem(Table table, String criteria, int points, String description) {
        table.addCell(new Cell().add(new Paragraph(criteria + "\n" + description)
                .setFontSize(10)));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(points))
                .setTextAlignment(TextAlignment.CENTER)));
        table.addCell(new Cell()); // Empty for tutor to fill
        table.addCell(new Cell()); // Empty for tutor to fill
    }

    private void addCommentsSection(Document document) {
        Paragraph commentsTitle = new Paragraph("Overall Comments:")
                .setBold()
                .setFontSize(12);
        document.add(commentsTitle);

        // Create a bordered area for comments
        Div commentsBox = new Div()
                .setHeight(100)
                .setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 1))
                .setPadding(5);
        document.add(commentsBox);
        document.add(new Paragraph("\n"));
    }

    public byte[] generateEvaluationGrid(Long defenseId) throws IOException {
        // Fetch defense data
        Defense defense = defenseRepository.findById(defenseId)
                .orElseThrow(() -> new NotFoundException("Defense not found"));

        User student = defense.getStudent();

        // PDF generation
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Set document margins
        document.setMargins(30, 30, 30, 30);

        // Add header with logo
        addHeader(document, defense, student);

        // Add evaluation criteria table
        addEvaluationTable(document);

        // Add comments section
        addCommentsSection(document);

        // Add generic signature section
        addGenericSignatureSection(document);

        // Add footer
        addFooter(document);

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

    private void addGenericSignatureSection(Document document) {
        float[] signatureWidths = {3, 1, 3};
        Table signatureTable = new Table(UnitValue.createPercentArray(signatureWidths));

        // Tutor name
        signatureTable.addCell(new Cell().add(new Paragraph("Tutor:"))
                .setBorderRight(Border.NO_BORDER));
        signatureTable.addCell(new Cell().add(new Paragraph(""))
                .setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER));
        signatureTable.addCell(new Cell().add(new Paragraph("_________________________"))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorderLeft(Border.NO_BORDER));

        // Signature line
        signatureTable.addCell(new Cell().add(new Paragraph("Signature:"))
                .setBorderRight(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER));
        signatureTable.addCell(new Cell().add(new Paragraph(""))
                .setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER));
        signatureTable.addCell(new Cell().add(new Paragraph("_________________________"))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorderLeft(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER));

        // Date
        signatureTable.addCell(new Cell().add(new Paragraph("Date:"))
                .setBorderRight(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER));
        signatureTable.addCell(new Cell().add(new Paragraph(""))
                .setBorderLeft(Border.NO_BORDER)
                .setBorderRight(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER));
        signatureTable.addCell(new Cell().add(new Paragraph("_________________________"))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorderLeft(Border.NO_BORDER)
                .setBorderTop(Border.NO_BORDER));

        document.add(signatureTable);
    }

    private void addFooter(Document document) {
        Paragraph footer = new Paragraph("Confidential - For academic use only")
                .setFontSize(8)
                .setFontColor(new DeviceRgb(150, 150, 150))
                .setTextAlignment(TextAlignment.CENTER);
        document.add(footer);
    }


    public ResponseEntity<byte[]> downloadEvaluationGrid(Long defenseId) throws IOException {
        byte[] pdfBytes = generateEvaluationGrid(defenseId);

        Defense defense = defenseRepository.findById(defenseId).orElseThrow();
        String studentName = defense.getStudent().getLastName() + "_" + defense.getStudent().getFirstName();
        String fileName = "EvaluationGrid_" + studentName + ".pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

}

