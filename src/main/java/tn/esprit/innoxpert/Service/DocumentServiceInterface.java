package tn.esprit.innoxpert.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.Document;
import java.io.IOException;
import java.util.List;

public interface DocumentServiceInterface {
    public List<Document> getAllDocuments();
    public Document getDocumentById(Long documentId);
    public Document addDocument(String name, String typeDocument, MultipartFile file) throws IOException;
    public void removeDocumentById(Long documentId);
    public Document  updateDocument (Document d );
    byte[] getDocumentFile(Long documentId) throws IOException;
    ResponseEntity<byte[]> downloadDocument(Long documentId) throws IOException;
    void saveDocument(String name, String typeDocument, MultipartFile file) throws IOException;


}
