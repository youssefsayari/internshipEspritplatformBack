package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.TimeLine;

import java.util.List;

@Repository
public interface TimeLineRepository extends JpaRepository<TimeLine,Long> {
    List<TimeLine> findByStudent_IdUser(Long studentId);

}
