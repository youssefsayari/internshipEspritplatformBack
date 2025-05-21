package tn.esprit.innoxpert.Service;



import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Repository.ClientRepository;
import tn.esprit.innoxpert.Repository.ConsultantRepository;
import tn.esprit.innoxpert.Repository.ConsultationRepository;
import tn.esprit.innoxpert.Repository.TimeSlotRepository;

import java.time.LocalDateTime;
import java.util.List;
@Service

public class ClientService {
    private final ClientRepository clientRepository;
    private final ConsultationRepository consultationRepository;
    private final ConsultantRepository consultantRepository;

    private final TimeSlotRepository timeSlotRepository;
    private final MailService mailService;
    private final MeetingService zoomService;
    public ClientService(ClientRepository clientRepository, ConsultantRepository consultantRepository, ConsultationRepository consultationRepository, TimeSlotRepository timeSlotRepository, MailService mailService, MeetingService zoomService) {
        this.clientRepository = clientRepository;
        this.consultantRepository = consultantRepository;
        this.consultationRepository = consultationRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.mailService = mailService;
        this.zoomService = zoomService;
    }

    // ➕ Create a new client
    public Client registerClient(Client client) {
        return clientRepository.save(client);
    }
    public List<Consultation> viewMyConsultations(Long clientId) {
        return consultationRepository.findByClientId(clientId);
    }

    // 🔍 Find a client by ID
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    // 📋 List all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // ✏️ Update client info
    public Client updateClient(Long id, Client updatedClient) {
        Client existingClient = getClientById(id);
        existingClient.setFullName(updatedClient.getFullName());
        existingClient.setEmail(updatedClient.getEmail());
        // Add other fields as needed
        return clientRepository.save(existingClient);
    }

    // ❌ Delete client
    public void deleteClient(Long id) {

        // Delete consultations first
        consultationRepository.deleteByClientId(id);

        // Then delete the client
        clientRepository.deleteById(id);
    }
    public boolean matchesSpecialty(String consultantSpecialties, String clientRequested) {
        List<String> consultantList = List.of(consultantSpecialties.toLowerCase().split(","));
        List<String> clientList = List.of(clientRequested.toLowerCase().split(","));

        // check if any client specialty is present in consultant's list
        return clientList.stream().anyMatch(clientSpec ->
                consultantList.stream().anyMatch(consultSpec -> consultSpec.contains(clientSpec.trim()))
        );
    }
    @Transactional
    public Consultation requestConsultation(Long clientId, String requestedSpecialty, LocalDateTime startTime, LocalDateTime endTime) throws MessagingException {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        List<Consultant> allConsultants = consultantRepository.findAll();

        for (Consultant consultant : allConsultants) {
            if (!matchesSpecialty(consultant.getSpecialty(), requestedSpecialty)) continue;

            // Check if the consultant has any overlapping consultations
            boolean isAvailable = timeSlotRepository
                    .findByConsultantAndStartTime(consultant, startTime)
                    .isEmpty();

            if (isAvailable) {
                // 1. Schedule the Zoom meeting using the default Zoom host
                String topic = "Consultation with " + client.getFullName();
                String meetingLink = zoomService.createScheduledZoomMeeting(topic, startTime, endTime);

                // 2. Save the time slot and mark it unavailable
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setStartTime(startTime);
                timeSlot.setEndTime(endTime);
                timeSlot.setAvailable(false);
                timeSlot.setConsultant(consultant);
                timeSlot = timeSlotRepository.save(timeSlot);

                // 3. Create and save the consultation
                Consultation consultation = new Consultation();
                consultation.setClient(client);
                consultation.setConsultant(consultant);
                consultation.setStatus(ConsultationStatus.ACCEPTED);
                consultation.setMeetingLink(meetingLink);
                consultation.setSlot(timeSlot);
                consultation = consultationRepository.save(consultation);

                // 4. Send email invitations to both parties
                mailService.sendMeetingInvitation(
                        client.getEmail(),
                        "Consultation Confirmed",
                        meetingLink,
                        startTime
                );

                mailService.sendMeetingInvitation(
                        consultant.getEmail(),
                        "New Consultation Scheduled",
                        meetingLink,
                        startTime
                );

                return consultation;
            }
        }

        throw new RuntimeException("No available consultants found for the requested time");
    }

}
