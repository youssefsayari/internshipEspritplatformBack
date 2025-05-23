package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DefenseRepository extends JpaRepository<Defense, Long> {
    boolean existsByStudent(User student);

    @Modifying
    @Query(value = "DELETE FROM defense_tutors WHERE defense_id = :defenseId", nativeQuery = true)
    void deleteDefenseTutorsByDefenseId(@Param("defenseId") Long defenseId);

    @Query("SELECT DISTINCT d FROM Defense d " +
            "LEFT JOIN FETCH d.student " +
            "LEFT JOIN FETCH d.tutors t " +
            "LEFT JOIN FETCH t.defenses")  // Add this join
    List<Defense> findAllWithStudentsAndTutors();
    @Query("SELECT d FROM Defense d WHERE d.classroom = :classroom AND d.defenseDate = :date")
    List<Defense> findByClassroomAndDefenseDate(
            @Param("classroom") String classroom,
            @Param("date") LocalDate date);

    @Query("SELECT d FROM Defense d JOIN d.tutors t WHERE t = :tutor AND d.defenseDate = :date")
    List<Defense> findByTutorsContainingAndDefenseDate(
            @Param("tutor") User tutor,
            @Param("date") LocalDate date);

    @Query("SELECT DISTINCT d FROM Defense d JOIN FETCH d.tutors t WHERE t.idUser = :tutorId")
    List<Defense> findDefensesByTutorId(@Param("tutorId") Long tutorId);

    @Query("SELECT DISTINCT d FROM Defense d JOIN FETCH d.student s WHERE s.idUser = :studentId")
    List<Defense> findDefensesByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT d FROM Defense d WHERE d.student.idUser = :studentId")
    Optional<Defense> findDefenseByStudentId(@Param("studentId") Long studentId);


}