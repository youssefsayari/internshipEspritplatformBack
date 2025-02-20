package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.User;

import java.util.List;

public interface CompanyServiceInterface {
    List<Company> getAllCompanies();
    Company getCompanyById(Long companyId);
    Company addCompanyAndAffectToNewUser(Company c);
    void removeCompanyByIdAndUserAffected(Long companyId);
    Company updateCompany(Company c);


    List<User> getCompanyFollowers(Long companyId);

}
