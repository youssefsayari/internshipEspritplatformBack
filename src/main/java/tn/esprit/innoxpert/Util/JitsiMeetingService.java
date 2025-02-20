package tn.esprit.innoxpert.Util;

import java.util.UUID;

public class JitsiMeetingService {

    public String generateMeetingLink(String title) {
        String roomName = title + "-" + UUID.randomUUID().toString();
        return "https://meet.jit.si/" + roomName;
    }
}
