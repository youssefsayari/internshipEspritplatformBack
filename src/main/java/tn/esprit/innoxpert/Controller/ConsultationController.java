package tn.esprit.innoxpert.Controller;


import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.ConsultationRequest;
import tn.esprit.innoxpert.DTO.ConsultationRequestDTO;
import tn.esprit.innoxpert.Entity.*;
import tn.esprit.innoxpert.Repository.ClientRepository;
import tn.esprit.innoxpert.Repository.ConsultantRepository;
import tn.esprit.innoxpert.Repository.ConsultationRepository;
import tn.esprit.innoxpert.Repository.TimeSlotRepository;
import tn.esprit.innoxpert.Service.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ConsultationController {

    private final ClientService clientService;
    private final ConsultantRepository consultantRepository;
    private final AiEmbeddingService aiEmbeddingService;
    private final ConsultationRepository consultationRepository;
    private final ConsultantService consultantService;
    private final MailService mailService;
    private final TimeSlotRepository timeSlotRepository;
    private final ClientRepository clientRepository;
    private final MeetingService meetingService;


    // ➕ Register new client
    @PostMapping
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.registerClient(client));
    }

    // 📋 Get all clients
    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    // 🔍 Get client by ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    // ✏️ Update client
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client updatedClient) {
        return ResponseEntity.ok(clientService.updateClient(id, updatedClient));
    }

    // ❌ Delete client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    // 📅 View client's consultations
    @GetMapping("/{clientId}/consultations")
    public ResponseEntity<List<Consultation>> viewMyConsultations(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.viewMyConsultations(clientId));
    }

    // 📆 Request consultation
    @PostMapping("/{clientId}/request-consultation")
    public ResponseEntity<Consultation> requestConsultation(
            @PathVariable Long clientId,
            @RequestParam String specialty,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
    ) throws MessagingException {
        Consultation consultation = clientService.requestConsultation(clientId, specialty, startTime, endTime);
        return ResponseEntity.ok(consultation);
    }

    @PostMapping("/match-consultants")
    public ResponseEntity<?> matchConsultants(@RequestBody ConsultationRequestDTO request) {
        try {
            List<Consultant> consultants = consultantRepository.findAll();

            // Extract profileDescriptions
            List<String> texts = consultants.stream()
                    .map(Consultant::getProfileDescription)
                    .toList();

            // Embed both the client's problem and consultant descriptions
            float[] clientEmbedding = aiEmbeddingService.getEmbedding(request.getProblemDescription());
            List<float[]> consultantEmbeddings = aiEmbeddingService.getEmbeddings(texts);

            // Match consultants by cosine similarity
            List<Map<String, Object>> matches = new ArrayList<>();
            for (int i = 0; i < consultants.size(); i++) {
                float score = similarity(clientEmbedding, consultantEmbeddings.get(i));
                Map<String, Object> match = new HashMap<>();
                match.put("consultant", consultants.get(i));
                match.put("score", score);
                matches.add(match);
            }

            // Sort by score descending
            matches.sort((a, b) -> Float.compare((float)b.get("score"), (float)a.get("score")));

            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    public float similarity(float[] a, float[] b) {
        float dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / ((float)(Math.sqrt(normA) * Math.sqrt(normB)) + 1e-10f);
    }
    @GetMapping("/consultations")
    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    // ✅ Get a specific consultation by ID
    @GetMapping("/consultation/{id}")
    public ResponseEntity<Consultation> getConsultationById(@PathVariable Long id) {
        return consultationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Optional: Filter by consultant
    @GetMapping("/by-consultant/{consultantId}")
    public List<Consultation> getByConsultant(@PathVariable Long consultantId) {
        return consultationRepository.findByConsultantId(consultantId);
    }

    // ✅ Optional: Filter by client
    @GetMapping("/by-client/{clientId}")
    public List<Consultation> getByClient(@PathVariable Long clientId) {
        return consultationRepository.findByClientId(clientId);
    }
    /*@GetMapping("/consultants/{consultantId}/availability")
    public List<LocalDateTime> getConsultantAvailability(
            @PathVariable Long consultantId,
            @RequestParam(defaultValue = "30") int durationMinutes) {
        return consultantService.getAvailableSlotsForConsultant(consultantId, Duration.ofMinutes(durationMinutes));
    }*/
    @PostMapping("/consultations/book")
    @Transactional
    public Consultation bookConsultation(
            @RequestBody ConsultationRequest request
    ) throws MessagingException {

        // 1. Get the client and consultant
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Consultant consultant = consultantRepository.findById(request.getConsultantId())
                .orElseThrow(() -> new RuntimeException("Consultant not found"));

        // 2. Check if consultant already has a consultation in the selected slot
        boolean isSlotTaken = consultationRepository.existsByConsultantAndSlotStartTime(
                consultant, request.getStartTime());

        if (isSlotTaken) {
            throw new RuntimeException("Time slot is already booked for this consultant");
        }

        // 3. Create new TimeSlot for this booking
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setConsultant(consultant);
        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setEndTime(request.getEndTime());
        timeSlot.setAvailable(false); // mark as used
        timeSlot = timeSlotRepository.save(timeSlot);

        // 4. Create the consultation
        Consultation consultation = new Consultation();
        consultation.setClient(client);
        consultation.setConsultant(consultant);
        consultation.setStatus(ConsultationStatus.ACCEPTED);
        consultation.setMeetingLink(meetingService.createScheduledZoomMeeting(
                "Consultation with " + client.getFullName(),
                request.getStartTime(),
                request.getEndTime()
        ));
        consultation.setSlot(timeSlot);
        consultation = consultationRepository.save(consultation);

        // 5. Notify both parties
        mailService.sendMeetingInvitation(client.getEmail(), "Consultation Confirmed",
                consultation.getMeetingLink(), request.getStartTime());

        mailService.sendMeetingInvitation(consultant.getEmail(), "New Consultation Scheduled",
                consultation.getMeetingLink(), request.getStartTime());

        return consultation;
    }


}


