package tn.esprit.innoxpert.Service;

import org.springframework.security.core.userdetails.UserDetails;

import tn.esprit.innoxpert.DTO.UserResponse;
import tn.esprit.innoxpert.Entity.Internship;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;
import java.util.Map;

public interface UserServiceInterface {
    public List<User> getAllUsers();
    public User getUserById(Long userId);
    public List<UserResponse> getUserBytypeUser(String TypeUser);
    public User addUser(User b);
    public void affectationTutor(Long userId, Long tutorId);
    public void updateTutorAdd(String Key, Long userId);
    public void updateTutorRem(String Key, Long userId);

    public void removeUserById(Long userId);
    public User updateUser (User b );
    public UserDetails loadUserByIdentifiant(String identifiant);
    public User getUserByIdentifiant(String identifiant) ;
   // public String decodeJwtToken(String token) ;
  // public Map<String, Object> decodeJwtWithoutVerification(String token) ;
    public String extractIdentifiantFromJwt(String token) ;
    public boolean validateOtp(String email, Long enteredOtp) ;
    public String generateOtp(String email);
    public boolean changePassword(String email, String newPassword) ;






    /*----------------start l5edmet sayari--------------------*/
    void followCompany(Long idUser, Long companyId);
    void unfollowCompany(Long idUser, Long companyId) ;
    List<Company> getFollowedCompanies(Long idUser);
    public TypeUser getUserType(Long idUser) ;

        /*----------------end l5edmet sayari--------------------*/


    }
