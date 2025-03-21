package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.Defense;
import tn.esprit.innoxpert.Service.DefenseServiceInterface;

import java.util.List;

@Tag(name = "Defense Management")
@RestController
@AllArgsConstructor
@RequestMapping("/defense")
public class DefenseRestController {

    @Autowired
    DefenseServiceInterface defenseService;

    @GetMapping("/getAllDefenses")
    public List<Defense> getAllDefenses() {
        return defenseService.getAllDefenses();
    }

    @GetMapping("/getDefenseById/{idDefense}")
    public Defense getDefenseById(@PathVariable("idDefense") Long idDefense) {
        return defenseService.getDfenseById(idDefense);
    }

    @PostMapping("/addDefense")
    public Defense addDefense(@RequestBody Defense defense) {
        return defenseService.addDefense(defense);
    }

    @DeleteMapping("/deleteDefense/{idDefense}")
    public void deleteDefenseById(@PathVariable("idDefense") Long idDefense) {
        defenseService.removeDefenseById(idDefense);
    }

    @PutMapping("/updateDefense")
    public Defense updateDefense(@RequestBody Defense defense) {
        return defenseService.updateDefense(defense);
    }
}
