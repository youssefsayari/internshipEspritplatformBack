package tn.esprit.innoxpert.Util;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JitsiMeetingService {

    public String generateMeetingLink(String title) {
        String roomName = title + "-" + UUID.randomUUID().toString();
        return "https://meet.jit.si/" + roomName;
    }
}
