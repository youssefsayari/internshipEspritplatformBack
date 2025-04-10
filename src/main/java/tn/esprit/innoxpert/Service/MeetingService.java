package tn.esprit.innoxpert.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Meeting;
import tn.esprit.innoxpert.Entity.TypeMeeting;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.MeetingRepository;
import tn.esprit.innoxpert.Repository.UserRepository;
import tn.esprit.innoxpert.Util.EmailClass;
import tn.esprit.innoxpert.Util.JitsiMeetingService;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class MeetingService implements MeetingServiceInterface {
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired

    JitsiMeetingService jitsiMeetingService ;
    private final EmailClass emailClass = new EmailClass();


    @Override
    public List<User> getStudentsByTutor(User tutor) {
        return userRepository.findByTutor(tutor);
    }

    @Override
    public List<Meeting> getAllMeetings() {
        return meetingRepository.getAllMeetingsOrderedByDate();    }

    @Override
    public Meeting getMeetingById(Long meetingId) {
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException("Meeting with ID : " + meetingId + " was not found."));
    }


    @Override
    public Meeting addMeeting(Meeting b) {
        if ((b.getTypeMeeting() == TypeMeeting.Restitution1 || b.getTypeMeeting() == TypeMeeting.Restitution2)
                && meetingRepository.existsByTypeMeeting(b.getTypeMeeting())) {
            throw new IllegalStateException("Cannot add more than one meeting of type " + b.getTypeMeeting());
        }

        b.setApproved(false);
        return meetingRepository.save(b);
    }


    @Override
    public void removeMeetingById(Long meetingId) {
        if (!meetingRepository.existsById(meetingId)) {
            throw new NotFoundException("Meeting with ID :  " + meetingId + " was not found.");
        }
        meetingRepository.deleteById(meetingId);
    }

    @Override
    public Meeting updateMeeting(Meeting b) {
        if (!meetingRepository.existsById(b.getIdMeeting())) {
            throw new NotFoundException("Meeting with ID: " + b.getIdMeeting() + " was not found. Cannot update.");
        }

        Meeting existingMeeting = meetingRepository.findById(b.getIdMeeting()).orElseThrow(() ->
                new NotFoundException("Meeting with ID: " + b.getIdMeeting() + " not found"));

        if ((b.getTypeMeeting() == TypeMeeting.Restitution1 || b.getTypeMeeting() == TypeMeeting.Restitution2)
                && !existingMeeting.getTypeMeeting().equals(b.getTypeMeeting())
                && meetingRepository.existsByTypeMeetingAndIdMeetingNot(b.getTypeMeeting(), b.getIdMeeting())) {
            throw new IllegalStateException("Cannot have more than one meeting of type " + b.getTypeMeeting());
        }

        return meetingRepository.save(b);
    }


    @Override
    public Meeting updateMeetingAndAffectToParticipant(Meeting meeting, Long organiserId, Long participantId) {
        Meeting existingMeeting = meetingRepository.findById(meeting.getIdMeeting())
                .orElseThrow(() -> new NotFoundException("Meeting with ID: " + meeting.getIdMeeting() + " not found"));

        User organiser = userRepository.findById(organiserId)
                .orElseThrow(() -> new NotFoundException("Organiser with ID: " + organiserId + " not found"));

        User participant = userRepository.findById(participantId)
                .orElseThrow(() -> new NotFoundException("Participant with ID: " + participantId + " not found"));

        if ((meeting.getTypeMeeting() == TypeMeeting.Restitution1 || meeting.getTypeMeeting() == TypeMeeting.Restitution2) &&
                meetingRepository.existsByTypeMeetingAndOrganiserAndParticipantAndIdMeetingNot(meeting.getTypeMeeting(), organiser, participant, meeting.getIdMeeting())) {
            throw new IllegalStateException("Each student can only have one " + meeting.getTypeMeeting() + " meeting per tutor.");
        }

        existingMeeting.setDate(meeting.getDate());
        existingMeeting.setHeure(meeting.getHeure());
        existingMeeting.setDescription(meeting.getDescription());
        existingMeeting.setTypeMeeting(meeting.getTypeMeeting());
        existingMeeting.setOrganiser(organiser);
        existingMeeting.setParticipant(participant);

        return meetingRepository.save(existingMeeting);
    }


    @Override
    public List<Meeting> findByParticipant(Long studentId) {
        return meetingRepository.findByParticipant_IdUser(studentId);
    }

    @Override
    public List<Meeting> findByOrganiser(Long tutorId) {
        return meetingRepository.findByOrganiser_IdUser(tutorId);
    }

    @Override
    public List<Meeting> findByParticipantAndOrganiser(Long studentId, Long organiserId) {
        return meetingRepository.findByParticipant_IdUserAndOrganiser_IdUser(studentId,organiserId);
    }

    @Override
    public Long findTutorIdByStudentId(Long studentId) {
        return userRepository.findTutorIdByStudentId(studentId);
    }

    @Override
    public Meeting approveMeeting(Meeting b) {
        if ( !meetingRepository.existsById(b.getIdMeeting())) {
            throw new NotFoundException("Meeting with ID: " + b.getIdMeeting() + " was not found. Cannot update.");
        }
        b.setApproved(true);
        return meetingRepository.save(b);
    }

    @Override
    public String generateMeeting(String title) {

        return new JitsiMeetingService().generateMeetingLink(title);
    }

    @Override
    public Meeting approveMeetingById(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException("Meeting with ID: " + meetingId + " was not found. Cannot update."));

        meeting.setApproved(true);
        return meetingRepository.save(meeting);
    }
    @Override
    public Meeting disapproveMeetingById(Long meetingId, String reason) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new NotFoundException("Meeting with ID: " + meetingId + " was not found. Cannot update."));

        meeting.setApproved(false);
        meeting.setNotified(false);
        meetingRepository.save(meeting);

        String participantEmail = meeting.getParticipant().getEmail();
        String subject = "⚠️ Réunion annulée - Notification officielle";

        String emailContent = String.format(
                "<html><body>"
                        + "<h2>🚨 Réunion Annulée</h2>"
                        + "<p>Bonjour %s,</p>"
                        + "<p>Nous vous informons que votre réunion prévue a été <b>annulée</b>.</p>"
                        + "<ul>"
                        + "<li><b>📌 Organisateur :</b> %s</li>"
                        + "<li><b>👤 Vous (Participant) :</b> %s</li>"
                        + "<li><b>📅 Date :</b> %s</li>"
                        + "<li><b>⏰ Heure :</b> %s</li>"
                        + "<li><b>📄 Type de Réunion :</b> %s</li>"
                        + "</ul>"
                        + "<p><b>📢 Raison de l'annulation :</b> %s</p>"
                        + "<p>❌ <b>Cette réunion ne sera pas effectuée.</b></p>"
                        + "<p>Si vous avez des questions, veuillez contacter votre organisateur.</p>"
                        + "<p>Cordialement,<br>Votre équipe.</p>"
                        + "</body></html>",
                meeting.getParticipant().getFirstName(),
                meeting.getOrganiser().getFirstName() + " " + meeting.getOrganiser().getLastName(),
                meeting.getParticipant().getFirstName() + " " + meeting.getParticipant().getLastName(),
                meeting.getDate(),
                meeting.getHeure(),
                meeting.getTypeMeeting(),
                reason
        );

        emailClass.sendHtmlEmail(participantEmail, subject, emailContent);

        System.out.println("✔️ Email de désapprobation envoyé à " + participantEmail + " avec la raison: " + reason);

        return meeting;
    }



    @Override
    public Meeting addMeetingAndAffectToParticipant(Meeting meeting, Long organiserId, Long participantId) {
        User organiser = userRepository.findById(organiserId)
                .orElseThrow(() -> new NotFoundException("Organiser with ID: " + organiserId + " not found"));
        User participant = userRepository.findById(participantId)
                .orElseThrow(() -> new NotFoundException("Participant with ID: " + participantId + " not found"));

        if ((meeting.getTypeMeeting() == TypeMeeting.Restitution1 || meeting.getTypeMeeting() == TypeMeeting.Restitution2) &&
                meetingRepository.existsByTypeMeetingAndOrganiserAndParticipant(meeting.getTypeMeeting(), organiser, participant)) {
            throw new IllegalStateException("Each student can only have one " + meeting.getTypeMeeting() + " meeting per tutor.");
        }

        meeting.setOrganiser(organiser);
        meeting.setParticipant(participant);

        String meetingTitle = meeting.getDescription().replaceAll("\\s+", "_") + "_" + meeting.getDate();
        meeting.setLink(jitsiMeetingService.generateMeetingLink(meetingTitle));

        return meetingRepository.save(meeting);
    }




//    @Scheduled(fixedRate = 10000) // ⏳ Vérifie toutes les heures (3600000 ms = 1 heure)
//    public void notifyUsersOneDayBeforeMeeting() {
//        LocalDate tomorrow = LocalDate.now().plusDays(1);
//
//        List<Meeting> meetings = meetingRepository.findByDateAndApprovedTrue(tomorrow);
//
//        for (Meeting meeting : meetings) {
//            String organiserEmail = meeting.getOrganiser().getEmail();
//            String participantEmail = meeting.getParticipant().getEmail();
//
//            String subject = "📅 Rappel de votre réunion prévue demain";
//            String message = "Bonjour,\n\n"
//                    + "Votre réunion est prévue demain à " + meeting.getHeure() + ".\n"
//                    + "🔗 Lien de la réunion : " + meeting.getLink() + "\n\n"
//                    + "Cordialement,\nVotre équipe.";
//
//            // Envoyer les e-mails
//            emailClass.envoyer(organiserEmail, message);
//            emailClass.envoyer(participantEmail, message);
//
//            System.out.println("✔️ Email envoyé à " + organiserEmail + " et " + participantEmail);
//        }
//
//        System.out.println("✔️ Vérification des réunions pour demain terminée !");
//    }


    @Override
    //@Scheduled(fixedRate = 10000)
    @Transactional
    public void notifyUsersOneDayBeforeMeeting() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Meeting> meetings = meetingRepository.findByDateAndApprovedTrueAndNotifiedFalse(tomorrow);

        if (meetings.isEmpty()) {
            System.out.println("✅ Aucun meeting à notifier.");
            return;
        }

        for (Meeting meeting : meetings) {
            String organiserEmail = meeting.getOrganiser().getEmail();
            String participantEmail = meeting.getParticipant().getEmail();

            String subject = "📅 Rappel : Réunion prévue demain à " + meeting.getHeure();
            String message = "<html><body>"
                    + "<h3>🔔 Rappel de votre réunion</h3>"
                    + "<p>Bonjour,</p>"
                    + "<p>Votre réunion est prévue demain :</p>"
                    + "<ul>"
                    + "<li>👨‍💼 <b>Organisateur :</b> " + meeting.getOrganiser().getFirstName() + " " + meeting.getOrganiser().getLastName() + "</li>"
                    + "<li>🙋 <b>Participant :</b> " + meeting.getParticipant().getFirstName() + " " + meeting.getParticipant().getLastName() + "</li>"
                    + "<li>📅 <b>Date :</b> " + meeting.getDate() + "</li>"
                    + "<li>⏰ <b>Heure :</b> " + meeting.getHeure() + "</li>"
                    + "<li>🔗 <b>Lien :</b> <a href='" + meeting.getLink() + "'>Rejoindre la réunion</a></li>"
                    + "</ul>"
                    + "<p>Merci de vous connecter à l'heure prévue.</p>"
                    + "<p>Cordialement,<br>Departement Stage.</p>"
                    + "</body></html>";

            emailClass.sendHtmlEmail(organiserEmail, subject, message);
            emailClass.sendHtmlEmail(participantEmail, subject, message);

            meeting.setNotified(true);
            meetingRepository.save(meeting);

            System.out.println("✔️ Email envoyé à " + organiserEmail + " et " + participantEmail);
        }

        System.out.println("✔️ Notifications envoyées !");
    }

    @Override
    @Transactional
    public List<User> getMostActiveStudents() {
        return meetingRepository.findMostActiveStudents();

    }

}
