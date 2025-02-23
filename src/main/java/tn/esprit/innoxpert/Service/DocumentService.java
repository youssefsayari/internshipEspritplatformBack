package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.DocumentRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class DocumentService implements DocumentServiceInterface {
    @Autowired
    DocumentRepository documentRepository;
    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentById(Long documentId) {
        return documentRepository.findById(documentId).get();
    }

    @Override
    public Document addDocument(Document b) {
       return documentRepository.save(b);
    }

    @Override
    public void removeDocumentById(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new NotFoundException("Document with ID :  " + documentId + " was not found.");
        }
         documentRepository.deleteById(documentId);

    }

    @Override
    public Document updateDocument(Document d) {
        if ( !documentRepository.existsById(d.getId())) {
            throw new NotFoundException("Document with ID: " + d.getId() + " was not found. Cannot update.");
        }
        return documentRepository.save(d);
    }
}
