package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

public interface DefenseRepository  extends JpaRepository<Defense,Long> {
    boolean existsByStudent(User student); // Custom method
    @Modifying
    @Query(value = "DELETE FROM defense_tutors WHERE defense_id = :defenseId", nativeQuery = true)
    void deleteDefenseTutorsByDefenseId(@Param("defenseId") Long defenseId);
    @Query("SELECT d FROM Defense d LEFT JOIN FETCH d.student LEFT JOIN FETCH d.tutors")
    List<Defense> findAllWithStudentsAndTutors();




}