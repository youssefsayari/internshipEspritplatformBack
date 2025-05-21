package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.innoxpert.Entity.TimeLine;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeLineRepository extends JpaRepository<TimeLine,Long> {
    List<TimeLine> findByStudent_IdUser(Long studentId);
    Optional<TimeLine> findByTitleAndStudent_IdUser(String title, Long userId);


}
