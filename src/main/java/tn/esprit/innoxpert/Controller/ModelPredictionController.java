package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Service.PmmlPredictionService;

@Tag(name = "ModelPrediction Management")
@RestController
@RequestMapping("/modelprediction")
public class ModelPredictionController {

    private final PmmlPredictionService predictor;

    public ModelPredictionController() throws Exception {
        this.predictor = new PmmlPredictionService();
    }

    @PostMapping
    public String predict(@RequestParam String option, @RequestParam String subject, @RequestParam String company) {
        try {
            double result = predictor.predict(option, subject, company);
            double percentage = result * 100;

            StringBuilder response = new StringBuilder();
            response.append(String.format("✅ Pourcentage de chance d'embauche : %.2f%%\n", percentage));

            if (percentage >= 70) {
                response.append("🌟 Très bonnes chances !");
            } else if (percentage >= 40) {
                response.append("💡 Chances modérées");
            } else {
                response.append("⚠️ Besoin d'amélioration");
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Erreur pendant la prédiction";
        }
    }
}
