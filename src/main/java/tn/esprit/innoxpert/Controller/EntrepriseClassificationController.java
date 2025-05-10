package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Service.XGBoostClassificationService;

@Tag(name = "Entreprise Classification")
@RestController
@RequestMapping("/classification")
public class EntrepriseClassificationController {

    private final XGBoostClassificationService classifier;

    public EntrepriseClassificationController(XGBoostClassificationService classifier) {
        this.classifier = classifier;
    }

    @PostMapping
    public String classifyEntreprise(
            @RequestParam String secteur,
            @RequestParam int annee,
            @RequestParam int employes,
            @RequestParam int estTech,
            @RequestParam double dynamisme
    ) {
        try {
            String result = classifier.classify(secteur, annee, employes, estTech, dynamisme);
            System.out.println(secteur);
            System.out.println(annee);
            System.out.println(employes);
            System.out.println(estTech);
            System.out.println(dynamisme);
            System.out.println(result);
            StringBuilder response = new StringBuilder();
            response.append("🏷️ Classe : ").append(result);
            if ("Startup".equalsIgnoreCase(result)) {
                response.append(" 🚀 Profil innovant et agile");
            } else {
                response.append(" 🏢 Structure établie");
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Erreur lors de la classification";
        }
    }
}
