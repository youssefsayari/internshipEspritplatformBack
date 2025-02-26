package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Service.DocumentServiceInterface;

import java.util.List;

@Tag(name = "Document Management")
@RestController
@AllArgsConstructor
@RequestMapping("/documents")
public class DocumentRestController {

    @Autowired
    private DocumentServiceInterface documentService;

    @GetMapping("/getAllDocuments")
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/getDocumentById/{idDocument}")
    public Document getDocumentById(@PathVariable("idDocument") Long idDocument) {
        return documentService.getDocumentById(idDocument);
    }

    @PostMapping("/addDocument")
    public Document addDocument(@RequestBody Document document) {
        return documentService.addDocument(document);
    }

    @DeleteMapping("/deleteDocument/{idDocument}")
    public void deleteDocumentById(@PathVariable("idDocument") Long idDocument) {
        documentService.removeDocumentById(idDocument);
    }

    @PutMapping("/updateDocument")
    public Document updateDocument(@RequestBody Document document) {
        return documentService.updateDocument(document);
    }
}
