package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.DTO.AgreementDTO;
import tn.esprit.innoxpert.DTO.AgreementRequestDTO;
import tn.esprit.innoxpert.DTO.InternshipDetailsDTO;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.AgreementRepository;
import tn.esprit.innoxpert.Repository.CompanyRepository;
import tn.esprit.innoxpert.Repository.InternshipRepository;
import tn.esprit.innoxpert.Repository.UserRepository;

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

    @Override
    public List<Agreement> getAllAgreements() {
        return List.of();
    }

    @Override
    public AgreementDTO getAgreementById(Long studentId) {
        Optional<Agreement> agreementOpt = agreementRepository.findByStudent_IdUser(studentId);

        if (agreementOpt.isPresent()) {
            Agreement agreement = agreementOpt.get();
            String companyName = agreement.getCompany().getName();
            String companyRepresentative = agreement.getCompany().getOwner().getFirstName() + " " + agreement.getCompany().getOwner().getLastName();

            return new AgreementDTO(
                    agreement.getId(),
                    agreement.getStartDate(),
                    agreement.getEndDate(),
                    companyName,
                    companyRepresentative,
                    agreement.getAgreementState()
            );
        }
        return null;
    }


    @Override
    public void addAgreement(AgreementRequestDTO agreementRequestDTO) {
        User student = userRepository.findById(agreementRequestDTO.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));
        Company company = companyRepository.findById(agreementRequestDTO.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not found"));
        Agreement agreement = new Agreement();
        agreement.setStartDate(agreementRequestDTO.getStartDate());
        agreement.setEndDate(agreementRequestDTO.getEndDate());
        agreement.setAgreementState(TypeAgreement.PENDING);
        agreement.setStudent(student);
        agreement.setCompany(company);
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
            throw new RuntimeException("Agreement is not in a state that can be approved.");
        }
        agreement.setAgreementState(TypeAgreement.ACCEPTED);
        agreementRepository.save(agreement);
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
