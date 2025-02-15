package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserServiceInterface {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long UserId) {
        return userRepository.findById(UserId)
                .orElseThrow(() -> new NotFoundException("User with ID : " + UserId + " was not found."));
    }

    @Override
    public User addUser(User b) {
        return userRepository.save(b);
    }

    @Override
    public void removeUserById(Long UserId) {
        if (!userRepository.existsById(UserId)) {
            throw new NotFoundException("User with ID :  " + UserId + " was not found.");
        }
        userRepository.deleteById(UserId);
    }

    @Override
    public User updateUser(User b) {
        if (!userRepository.existsById(b.getIdUser())) {
            throw new NotFoundException("User with ID: " + b.getIdUser() + " was not found. Cannot update.");
        }
        return userRepository.save(b);
    }

    @Override
    public UserDetails loadUserByIdentifiant(String identifiant) {
        User user = userRepository.findByIdentifiant(identifiant)
                .orElseThrow(() -> new NotFoundException("User not found with identifiant: " + identifiant));
        return new org.springframework.security.core.userdetails.User(user.getIdentifiant(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
