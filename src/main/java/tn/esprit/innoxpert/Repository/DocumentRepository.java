package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Document;
import tn.esprit.innoxpert.Entity.Internship;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
}