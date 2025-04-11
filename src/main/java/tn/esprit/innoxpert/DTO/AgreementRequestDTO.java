package tn.esprit.innoxpert.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import tn.esprit.innoxpert.Entity.TypeAgreement;

import java.util.Date;

public class AgreementRequestDTO {
    private Long studentId;
    private Long companyId;
    private Long postId;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
