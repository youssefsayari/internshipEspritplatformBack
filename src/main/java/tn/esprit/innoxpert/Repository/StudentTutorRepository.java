package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.StudentTutor;

import java.util.List;

@Repository
public interface StudentTutorRepository extends JpaRepository<StudentTutor, Long> {
    @Query("SELECT st FROM StudentTutor st WHERE st.tutor.idUser = :tutorId")
    List<StudentTutor> findByTutorId(@Param("tutorId") Long tutorId);
    @Query("SELECT st.tutor.idUser FROM StudentTutor st WHERE st.student.idUser = :studentId")
    Long findTutorIdByStudentId(@Param("studentId") Long studentId);

}

