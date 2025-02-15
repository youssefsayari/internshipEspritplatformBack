package tn.esprit.innoxpert.Service;

import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

public interface UserServiceInterface {
    public List<User> getAllUsers();
    public User getUserById(Long userId);
    public User addUser(User b);
    public void removeUserById(Long userId);
    public User updateUser (User b );
    public UserDetails loadUserByIdentifiant(String identifiant);
}
