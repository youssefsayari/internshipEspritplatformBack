package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.DTO.TimeLineRequest;
import tn.esprit.innoxpert.DTO.TimeLineResponse;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Entity.TimeLine;
import tn.esprit.innoxpert.Entity.TypeDocument;
import tn.esprit.innoxpert.Service.AgreementService;
import tn.esprit.innoxpert.Service.DocumentService;
import tn.esprit.innoxpert.Service.TimeDocumentService;
import tn.esprit.innoxpert.Service.TimeLineService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "TimeLine Management")
@RestController
@AllArgsConstructor
@RequestMapping("/timeline")
public class TimeLineRestController {
    @Autowired
    TimeLineService timeLineService;
    @Autowired
    TimeDocumentService documentService;

    @PostMapping("/add")
    public ResponseEntity<Void> addTimeLine(@RequestBody TimeLineRequest timeLineRequest) {
        timeLineService.addTimeLine(timeLineRequest.getUserId(), timeLineRequest.getAgreementId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public List<TimeLineResponse> getTimeLinesByUserId(@PathVariable Long userId) {
        return timeLineService.getTimeLinesByUserId(userId);
    }

    @PutMapping("/accept-step")
    public ResponseEntity<String> acceptStep(@RequestParam String title, @RequestParam Long userId,@RequestParam Integer note) {
        timeLineService.acceptStep(title, userId,note);
        return ResponseEntity.ok("Step accepted successfully.");
    }

    @PutMapping("/reject-step")
    public ResponseEntity<String> rejectStep(@RequestParam String title, @RequestParam Long userId, @RequestParam Integer note) {
        timeLineService.rejectStep(title, userId, note);
        return ResponseEntity.ok("Step rejected successfully.");
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file,
                                            @RequestParam("type") TypeDocument typeDocument,
                                            @RequestParam("studentId") Long studentId,
                                            @RequestParam("nomEtape") String nomEtape) {
        try {
            Document savedDoc = documentService.saveDocument(file, typeDocument, studentId, nomEtape);
            return ResponseEntity.ok().body(Map.of("message", "Upload successful", "document", savedDoc));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }
    @CrossOrigin(exposedHeaders = "Content-Disposition")
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) throws IOException {
        return documentService.downloadDocument(id);
    }



}
