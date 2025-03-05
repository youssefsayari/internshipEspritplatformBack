package tn.esprit.innoxpert.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByIdentifiant(String identifiant);
    List<User> findByTutor(User tutor);


    List<User> findByTypeUser(TypeUser typeUser);
    List<User> findByTutor_IdUser(Long idUser);

}
