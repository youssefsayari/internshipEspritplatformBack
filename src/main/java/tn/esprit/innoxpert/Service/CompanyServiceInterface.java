package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Company;

import java.util.List;

public interface CompanyServiceInterface {
    public List<Company> getAllCompanies();
    public Company getCompanyById(Long TaskId);
    public Company addCompany(Company c);
    public void removeCompanyById(Long CompanyId);
    public Company updateCompany (Company c );
    public Company addAndaffectCompanyToStudent(Long idUser,Company newCompany);
}
