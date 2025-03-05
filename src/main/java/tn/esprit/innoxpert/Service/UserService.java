package tn.esprit.innoxpert.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Repository.CompanyRepository;
import tn.esprit.innoxpert.Repository.UserRepository;
import tn.esprit.innoxpert.Util.JwtUtil;

import java.util.*;


@Service
@AllArgsConstructor
public class UserService implements UserServiceInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JwtUtil jwtUtil;

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

    @Override
    public User getUserByIdentifiant(String identifiant) {
        return userRepository.findByIdentifiant(identifiant).get();

    }

   /* @Override
    public String decodeJwtToken(String token) {
        try {
            return jwtUtil.extractIdentifiant(token);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired token: " + e.getMessage());
        }
    }*/

    /*public Map<String, Object> decodeJwtWithoutVerification(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT format");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(payloadJson, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid or malformed JWT: " + e.getMessage());
        }
    }*/
    public String extractIdentifiantFromJwt(String token) {
        try {
            String[] parts = token.split("\\."); // Split JWT (Header, Payload, Signature)
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT format");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1])); // Decode Payload
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payloadMap = objectMapper.readValue(payloadJson, Map.class); // Convert JSON to Map

            return (String) payloadMap.get("sub"); // Extract "sub" field (Identifiant)
        } catch (Exception e) {
            throw new RuntimeException("Invalid or malformed JWT: " + e.getMessage());
        }
    }




    /*----------------start l5edmet sayari--------------------*/

    @Override
    public void followCompany(Long idUser, Long companyId) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User not found"));
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));

        if (!user.getFollowedCompanies().contains(company)) {
            user.getFollowedCompanies().add(company);
            userRepository.save(user);
        }
    }

    @Override
    public void unfollowCompany(Long idUser, Long companyId) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        if (user.getFollowedCompanies().contains(company)) {
            user.getFollowedCompanies().remove(company);
            userRepository.save(user);
        }
    }

    @Override
    public List<Company> getFollowedCompanies(Long idUser) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFollowedCompanies();
    }
    @Override
    public TypeUser getUserType(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        return user.map(User::getTypeUser).orElse(null);
    }

    /*----------------end l5edmet sayari--------------------*/


}
