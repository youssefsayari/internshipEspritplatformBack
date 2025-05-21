package tn.esprit.innoxpert.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.innoxpert.Entity.Skill;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostAdminResponse {
    private Long id;
    private String title;
    private String content;
    private String companyName;
    private Date createdAt;
    private String typeInternship;
    List<Skill> skills;
}
