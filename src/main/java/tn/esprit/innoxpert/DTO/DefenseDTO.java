package tn.esprit.innoxpert.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DefenseDTO {
    private Long idDefense;
    private LocalDate defenseDate;
    private LocalTime defenseTime;
    private String classroom;
    private boolean reportSubmitted;
    private boolean internshipCompleted;
    private double defenseDegree;
    private UserDTO student;
    private List<UserDTO> tutors;

    // Getters and Setters

    public Long getIdDefense() {
        return idDefense;
    }

    public void setIdDefense(Long idDefense) {
        this.idDefense = idDefense;
    }

    public LocalDate getDefenseDate() {
        return defenseDate;
    }

    public void setDefenseDate(LocalDate defenseDate) {
        this.defenseDate = defenseDate;
    }

    public LocalTime getDefenseTime() {
        return defenseTime;
    }

    public void setDefenseTime(LocalTime defenseTime) {
        this.defenseTime = defenseTime;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public boolean isReportSubmitted() {
        return reportSubmitted;
    }

    public void setReportSubmitted(boolean reportSubmitted) {
        this.reportSubmitted = reportSubmitted;
    }

    public boolean isInternshipCompleted() {
        return internshipCompleted;
    }

    public void setInternshipCompleted(boolean internshipCompleted) {
        this.internshipCompleted = internshipCompleted;
    }

    public double getDefenseDegree() {
        return defenseDegree;
    }

    public void setDefenseDegree(double defenseDegree) {
        this.defenseDegree = defenseDegree;
    }

    public UserDTO getStudent() {
        return student;
    }

    public void setStudent(UserDTO student) {
        this.student = student;
    }

    public List<UserDTO> getTutors() {
        return tutors;
    }

    public void setTutors(List<UserDTO> tutors) {
        this.tutors = tutors;
    }
}

