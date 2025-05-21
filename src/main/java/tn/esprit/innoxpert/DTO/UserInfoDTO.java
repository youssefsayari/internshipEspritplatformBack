package tn.esprit.innoxpert.DTO;

import tn.esprit.innoxpert.Entity.Expertise;
import tn.esprit.innoxpert.Entity.UserInfo;

import java.util.List;

public class UserInfoDTO {

    private Long idUserDetail;
    private List<Expertise> expertises;

    // Constructor to map from UserInfo entity
    public UserInfoDTO(UserInfo userInfo) {
        this.idUserDetail = userInfo.getIdUserDetail();
        this.expertises = userInfo.getExpertises(); // You can filter/exclude some expertises if needed
    }

    // Getters and Setters

    public Long getIdUserDetail() {
        return idUserDetail;
    }

    public void setIdUserDetail(Long idUserDetail) {
        this.idUserDetail = idUserDetail;
    }

    public List<Expertise> getExpertises() {
        return expertises;
    }

    public void setExpertises(List<Expertise> expertises) {
        this.expertises = expertises;
    }
}
