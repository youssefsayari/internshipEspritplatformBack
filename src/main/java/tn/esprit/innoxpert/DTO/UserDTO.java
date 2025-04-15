package tn.esprit.innoxpert.DTO;

import tn.esprit.innoxpert.Entity.User;

public class UserDTO {

    private Long idUser;
    private String firstName;
    private String lastName;
    private String email;
    private Long telephone;
    private UserInfoDTO userInfo;

    // Constructor to map from User entity
    public UserDTO(User user) {
        this.idUser = user.getIdUser();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.telephone = user.getTelephone();
        this.userInfo = new UserInfoDTO(user.getUserInfo());
    }
    public UserDTO() {} // required for JSON serialization


    // Getters and Setters

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTelephone() {
        return telephone;
    }

    public void setTelephone(Long telephone) {
        this.telephone = telephone;
    }

    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }
}