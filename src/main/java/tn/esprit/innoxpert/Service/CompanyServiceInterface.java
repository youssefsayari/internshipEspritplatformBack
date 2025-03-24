package tn.esprit.innoxpert.Service;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.User;

import java.io.IOException;
import java.util.List;

public interface CompanyServiceInterface {
    List<Company> getAllCompanies();
    Company getCompanyById(Long companyId);
    public Company addCompanyAndAffectToNewUser(Company c, MultipartFile file) throws IOException;
    void removeCompanyByIdAndUserAffected(Long companyId);
     Company updateCompany(Long companyId, Company updatedData);
     Long getCompanyIdByUserId(Long userId) ;
    public Boolean IsCompany(Long userId) ;
        List<User> getCompanyFollowers(Long companyId);
    void followCompany(Long userId, Long companyId) ;
    void unfollowCompany(Long userId, Long companyId) ;
}
