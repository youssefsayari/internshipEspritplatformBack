package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.Task;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByStudent(User student);
    @Query("SELECT t.student, COUNT(t) FROM Task t WHERE t.status = 'DONE' GROUP BY t.student ORDER BY COUNT(t) DESC LIMIT 1")
    User findStudentWithMostDoneTasks();

    @Query("SELECT COUNT(t) FROM Task t WHERE t.student = :student AND t.status = tn.esprit.innoxpert.Entity.TypeStatus.DONE")
    int countDoneTasksByStudent(User student);




}
