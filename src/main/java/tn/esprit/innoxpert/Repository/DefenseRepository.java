package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.User;

public interface DefenseRepository  extends JpaRepository<Defense,Long> {
    boolean existsByStudent(User student); // Custom method

}
