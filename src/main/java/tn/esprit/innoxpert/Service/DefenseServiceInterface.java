package tn.esprit.innoxpert.Service;

import tn.esprit.innoxpert.Entity.Defense;
import java.util.List;

public interface DefenseServiceInterface {
    public List<Defense> getAllDefenses();
    public Defense getDfenseById(Long idDefense);
    public Defense addDefense(Defense d);
    public void removeDefenseById(Long idDefense);
    public Defense updateDefense (Defense d );

}
