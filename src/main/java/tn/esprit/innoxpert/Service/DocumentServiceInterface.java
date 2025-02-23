package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Entity.Meeting;

import javax.print.Doc;
import java.util.List;

public interface DocumentServiceInterface {
    public List<Document> getAllDocuments();
    public Document getDocumentById(Long documentId);
    public Document addDocument(Document d);
    public void removeDocumentById(Long documentId);
    public Document  updateDocument (Document d );

}
