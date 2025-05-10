package tn.esprit.innoxpert.DTO;

import jakarta.validation.constraints.*;
import tn.esprit.innoxpert.Entity.TypeSector;
import java.util.Date;

public class CompanyAnalyticsDto {
    private Long id;
    private String name;
    private String address;
    private TypeSector sector;
    private String logoUrl;
    private String website;
    private Long internshipCount;
    private Double averageRating;
    private Date foundingYear;
    private Date labelDate;
    private String founders;
    private String email;
    private Integer numEmployees; // Nouveau champ ajouté



    // Constructeur par défaut
    public CompanyAnalyticsDto() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public TypeSector getSector() { return sector; }
    public void setSector(TypeSector sector) { this.sector = sector; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public Long getInternshipCount() { return internshipCount; }
    public void setInternshipCount(Long internshipCount) { this.internshipCount = internshipCount; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Date getFoundingYear() { return foundingYear; }
    public void setFoundingYear(Date foundingYear) { this.foundingYear = foundingYear; }

    public Date getLabelDate() { return labelDate; }
    public void setLabelDate(Date labelDate) { this.labelDate = labelDate; }

    public String getFounders() { return founders; }
    public void setFounders(String founders) { this.founders = founders; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email= email; }

    // Getters et Setters pour le nouveau champ
    public Integer getNumEmployees() { return numEmployees; }
    public void setNumEmployees(Integer numEmployees) {
        this.numEmployees = numEmployees;
    }
}