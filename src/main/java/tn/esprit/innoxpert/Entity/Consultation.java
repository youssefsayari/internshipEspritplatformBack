package tn.esprit.innoxpert.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultant_id")
    private Consultant consultant;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private TimeSlot slot;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status = ConsultationStatus.PENDING;

    private String meetingLink;

    private LocalDateTime createdAt = LocalDateTime.now();
}