package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.TypeDocument;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d WHERE d.typeDocument = :typeDocument AND d.student.idUser = :userId AND d.isDownloadable = :isDownloadable")
    Optional<Document> findByTypeDocumentAndUser_IdUserAndIsDownloadable(
            @Param("typeDocument") TypeDocument typeDocument,
            @Param("userId") Long userId,
            @Param("isDownloadable") boolean isDownloadable);
}
