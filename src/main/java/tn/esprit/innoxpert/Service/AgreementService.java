package tn.esprit.innoxpert.Service;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.AgreementDTO;
import tn.esprit.innoxpert.DTO.AgreementRequestDTO;
import tn.esprit.innoxpert.DTO.InternshipDetailsDTO;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.*;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgreementService implements AgreementServiceInterface {
    @Autowired
    AgreementRepository agreementRepository;
    InternshipRepository internshipRepository;
    private UserRepository userRepository;
    CompanyRepository companyRepository;
    AgreementRemarkService agreementRemarkService;

    @Override
    public List<Agreement> getAllAgreements() {
        return List.of();
    }

    @Override
    public AgreementDTO getAgreementById(Long studentId) {
        Optional<Agreement> agreementOpt = agreementRepository.findByStudent_IdUser(studentId);

        if (agreementOpt.isPresent()) {
            Agreement agreement = agreementOpt.get();
            Internship internship = internshipRepository.findById(agreement.getPostId()).orElse(null);
            String nomValidateur = internship.getValidator().getFirstName() + " " + internship.getValidator().getLastName();
            Long validateurId = internship.getValidator().getIdUser();
            String companyName = agreement.getCompany().getName();
            String companyRepresentative = agreement.getCompany().getOwner().getFirstName() + " " + agreement.getCompany().getOwner().getLastName();

            return new AgreementDTO(
                    agreement.getId(),
                    agreement.getStartDate(),
                    agreement.getEndDate(),
                    companyName,
                    companyRepresentative,
                    agreement.getAgreementState(),
                    agreement.getCreationDate(),
                    nomValidateur,validateurId
            );
        }
        return null;
    }

    @Transactional
    @Override
    public void addAgreement(AgreementRequestDTO agreementRequestDTO) {

        User student = userRepository.findById(agreementRequestDTO.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));
        Company company = companyRepository.findById(agreementRequestDTO.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not found"));

        Optional<Agreement> existingAgreementOpt = agreementRepository.findByStudent_IdUser(student.getIdUser());

        if (existingAgreementOpt.isPresent()) {
            System.out.println("✅ Existing agreement found for student with ID: " + student.getIdUser());

            Agreement existingAgreement = existingAgreementOpt.get();
            if (!existingAgreement.getAgreementState().equals(TypeAgreement.REJECTED)) {
                throw new IllegalStateException("You already have a pending or approved agreement.");
            }

            agreementRemarkService.removeAgreementRemarkByAgreemntID(existingAgreement.getId());
            agreementRepository.delete(existingAgreement);
            agreementRepository.flush();
            Optional<Agreement> deletedAgreement = agreementRepository.findById(existingAgreement.getId());
            if (deletedAgreement.isPresent()) {
                throw new IllegalStateException("Failed to delete existing agreement.");
            }
        }

        Agreement agreement = new Agreement();
        agreement.setStartDate(agreementRequestDTO.getStartDate());
        agreement.setEndDate(agreementRequestDTO.getEndDate());
        agreement.setAgreementState(TypeAgreement.PENDING);
        agreement.setCreationDate(new Date());
        agreement.setStudent(student);
        agreement.setCompany(company);
        agreement.setPostId(agreementRequestDTO.getPostId());

        agreementRepository.save(agreement);
    }


    @Override
    public boolean hasApprovedInternship(Long studentId) {
        return internshipRepository.existsByStudentIdAndState(studentId, InternshipState.APPROVED);
    }

    @Override
    public List<InternshipDetailsDTO> getInternshipsForStudent(Long studentId) {
        List<Internship> internships = internshipRepository.findInternshipsByStudentIdAndState(studentId, InternshipState.APPROVED);
        return internships.stream()
                .map(internship -> {
                    InternshipDetailsDTO dto = new InternshipDetailsDTO();
                    dto.setInternshipTitle(internship.getTitle());

                    if (internship.getPost() != null) {
                        dto.setCompanyName(internship.getPost().getCompany().getName());
                        dto.setCompanyAddress(internship.getPost().getCompany().getAddress());
                        dto.setCompanyId(internship.getPost().getCompany().getId());
                        dto.setComponyPhone(internship.getPost().getCompany().getPhone());
                        dto.setPostId(internship.getId());

                        if (internship.getPost().getCompany().getOwner() != null) {
                            String fullName = internship.getPost().getCompany().getOwner().getFirstName() + " " + internship.getPost().getCompany().getOwner().getLastName();
                            dto.setCompanyRepresentativeFullName(fullName);
                        }
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void approveAgreement(Long AgreementId) {
        Agreement agreement = agreementRepository.findById(AgreementId)
                .orElseThrow(() -> new RuntimeException("Agreement not found"));
        if (!agreement.getAgreementState().equals(TypeAgreement.PENDING)) {
            throw new RuntimeException("Agreement is not in a state that can be accepted.");
        }
        agreement.setAgreementState(TypeAgreement.ACCEPTED);
        agreementRepository.save(agreement);
    }

    @Override
    public void acceptAgreement(Long AgreementId) {
        Agreement agreement = agreementRepository.findById(AgreementId)
                .orElseThrow(() -> new RuntimeException("Agreement not found"));
        if (!agreement.getAgreementState().equals(TypeAgreement.ACCEPTED)) {
            throw new RuntimeException("Agreement is not in a state that can be approved.");
        }
        agreement.setAgreementState(TypeAgreement.APPROVED);
        agreementRepository.save(agreement);
    }

    @Override
    public void removeAgreement(Long id) {
        Agreement agreement = agreementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agreement not found"));
        agreementRepository.delete(agreement);
    }

    @Override
    public void GeneretePDF(Long AgreementId) {
        Optional<Agreement> agreementOpt = agreementRepository.findById(AgreementId);
        if (!agreementOpt.isPresent()) {
            throw new IllegalArgumentException("Agreement not found!");
        }

        Agreement agreement = agreementOpt.get();

        String studentName = agreement.getStudent().getFirstName() + " " + agreement.getStudent().getLastName();
        String companyName = agreement.getCompany().getName();
        String companyAddress = agreement.getCompany().getAddress();
        String companyPhone = String.valueOf(agreement.getCompany().getPhone());
        String companyRepresentative = agreement.getCompany().getOwner().getFirstName() + " " + agreement.getCompany().getOwner().getLastName();
        Date startDate = agreement.getStartDate();
        Date endDate = agreement.getEndDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);


        String userDesktopPath = System.getProperty("user.home") + "\\Desktop\\";
        String fileName = "Convention_Stage_" + AgreementId + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(userDesktopPath + fileName);
            PdfDocument pdf = new PdfDocument(writer);
            PdfFont font = PdfFontFactory.createFont();
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);

            // Centred title
            Paragraph title = new Paragraph("CONVENTION DE STAGE")
                    .setFont(font)
                    .setFontSize(16)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);

            // Main content
            document.add(new Paragraph("Entre :").setFont(font));
            document.add(new Paragraph("L'ECOLE SUPERIEURE PRIVEE D'INGENIERIE ET DE TECHNOLOGIES, ESPRIT").setFont(font));
            document.add(new Paragraph("Représentée par son Directeur Fondateur Pre Tahar Ben Lakhdar.").setFont(font));
            document.add(new Paragraph("Ci-après dénommée ESPRIT.").setFont(font));

            // Société info (smaller font)
            document.add(new Paragraph("La Société : " + companyName).setFont(font).setFontSize(10));
            document.add(new Paragraph("Adresse : " + companyAddress).setFont(font).setFontSize(10));
            document.add(new Paragraph("Téléphone : " + companyPhone).setFont(font).setFontSize(10));
            document.add(new Paragraph("Représenté(e) par : " + companyRepresentative).setFont(font).setFontSize(10));
            document.add(new Paragraph("Ci-après dénommé(e) l'entreprise.").setFont(font));

            // Student info
            document.add(new Paragraph("L'étudiant(e): " + studentName + ", inscrit(e) à ESPRIT en 3ème année cycle ingénieur, en vue d'obtenir le Diplôme National d'Ingénieur en Informatique.").setFont(font));
            document.add(new Paragraph("Ci-après dénommé(e) l'étudiant(e).").setFont(font));
            document.add(new Paragraph("Il a été arrêté et convenu ce qui suit :").setFont(font));

            // Articles
            document.add(new Paragraph("ARTICLE 1 (Objet de la convention)").setFont(font).setBold());
            document.add(new Paragraph("La convention règle les rapports entre ESPRIT et l'étudiant concernant le stage effectué au sein de l'entreprise.").setFont(font));

            document.add(new Paragraph("ARTICLE 2 (Objectif du PFE)").setFont(font).setBold());
            document.add(new Paragraph("Le Stage de PFE a pour objectif la mise en pratique des compétences acquises.").setFont(font));

            document.add(new Paragraph("ARTICLE 3").setFont(font).setBold());
            document.add(new Paragraph("Le programme du PFE fera l'objet d'une étude par le comité de validation d'ESPRIT en concertation avec l'encadrant de l'entreprise.").setFont(font));

            document.add(new Paragraph("ARTICLE 4 (Statut de l'étudiant)").setFont(font).setBold());
            document.add(new Paragraph("Pendant son séjour en entreprise, l'étudiant conserve son statut d'étudiant. Il doit se conformer au règlement intérieur de l'entreprise.").setFont(font));

            document.add(new Paragraph("ARTICLE 5 (Résiliation)").setFont(font).setBold());
            document.add(new Paragraph("5.1 Le stagiaire peut rompre la convention de stage après avoir informé son maître de stage et la direction d'ESPRIT.").setFont(font));
            document.add(new Paragraph("5.2 Le stage peut être suspendu pour des raisons médicales. Dans ce cas, un avenant précisant les nouvelles conditions sera signé.").setFont(font));

            // Ajout de l'ARTICLE 11
            document.add(new Paragraph("ARTICLE 11 (Organisation du stage)").setFont(font).setBold());
            document.add(new Paragraph("Le stage se déroulera selon l'organisation suivante :").setFont(font));
            document.add(new Paragraph("Le stage aura lieu du : " + formattedStartDate + " au " + formattedEndDate).setFont(font));
            document.add(new Paragraph("- Département : Technology Strategy & Transformation").setFont(font));
            document.add(new Paragraph("- Service : Informatique").setFont(font));
            document.add(new Paragraph("- Encadrant technique dans l'entreprise : " + companyRepresentative).setFont(font));

            // Ajout du tableau de signatures
            Table table = new Table(3);
            table.setWidth(UnitValue.createPercentValue(100));
            table.setTextAlignment(TextAlignment.CENTER); // Applique à tout le tableau

            float cellHeight = 100f; // Hauteur personnalisée pour améliorer le centrage vertical
            TextAlignment align = TextAlignment.CENTER;
            VerticalAlignment valign = VerticalAlignment.MIDDLE;

            String col1 = "A ........................... LE  ..../..../......\n\n"
                    + "Le directeur de l'entreprise (ou Organisme)\n"
                    + "ou représentant et cachet";
            table.addCell(new Cell().add(new Paragraph(col1).setFont(font).setFontSize(9).setTextAlignment(align))
                    .setVerticalAlignment(valign)
                    .setBorder(Border.NO_BORDER)
                    .setHeight(cellHeight));

            String todayFormatted = dateFormat.format(new Date());
            String col2 = "A Ariana le " + todayFormatted + "\n\n"
                    + "Le Directeur d'ESPRIT\n"
                    + "P/Pr. Tahar BENLAKHDAR";
            table.addCell(new Cell().add(new Paragraph(col2).setFont(font).setFontSize(9).setTextAlignment(align))
                    .setVerticalAlignment(valign)
                    .setBorder(Border.NO_BORDER)
                    .setHeight(cellHeight));

            String col3 = "A Ariana le " + todayFormatted + "\n\n"
                    + "L'étudiant(e)";
            table.addCell(new Cell().add(new Paragraph(col3).setFont(font).setFontSize(9).setTextAlignment(align))
                    .setVerticalAlignment(valign)
                    .setBorder(Border.NO_BORDER)
                    .setHeight(cellHeight));

            document.add(new Paragraph("\n\n"));
            document.add(table);

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF");
        }
    }



    @Override
    public void rejectAgreement(Long AgreementId) {

        Agreement agreement = agreementRepository.findById(AgreementId)
                .orElseThrow(() -> new RuntimeException("Agreement not found"));
        if (!agreement.getAgreementState().equals(TypeAgreement.PENDING)) {
            throw new RuntimeException("Agreement is not in a state that can be rejected.");
        }
        agreement.setAgreementState(TypeAgreement.REJECTED);
        agreementRepository.save(agreement);
    }



}
