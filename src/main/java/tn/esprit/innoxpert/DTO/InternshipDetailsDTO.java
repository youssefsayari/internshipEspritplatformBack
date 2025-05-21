package tn.esprit.innoxpert.DTO;

public class InternshipDetailsDTO {
    private String internshipTitle;
    private String companyName;
    private String companyAddress;
    private String companyRepresentativeFullName;
    private Long companyId;
    private Long componyPhone;
    private Long postId;

    public String getInternshipTitle() {
        return internshipTitle;
    }

    public void setInternshipTitle(String internshipTitle) {
        this.internshipTitle = internshipTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyRepresentativeFullName() {
        return companyRepresentativeFullName;
    }

    public void setCompanyRepresentativeFullName(String companyRepresentativeFullName) {
        this.companyRepresentativeFullName = companyRepresentativeFullName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getComponyPhone() {
        return componyPhone;
    }

    public void setComponyPhone(Long componyPhone) {
        this.componyPhone = componyPhone;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
