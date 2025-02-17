package tn.esprit.innoxpert.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.innoxpert.Entity.Company;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService implements CompanyServiceInterface {

    @Override
    public List<Company> getAllCompanies() {
        return List.of();
    }

    @Override
    public Company getCompanyById(Long TaskId) {
        return null;
    }

    @Override
    public Company addCompany(Company c) {
        return null;
    }

    @Override
    public void removeCompanyById(Long CompanyId) {

    }

    @Override
    public Company updateCompany(Company c) {
        return null;
    }

    @Override
    public Company addAndaffectCompanyToStudent(Long idUser, Company newCompany) {
        return null;
    }
}
