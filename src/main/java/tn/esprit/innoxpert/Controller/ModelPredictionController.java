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
            response.append(String.format("📊 Hiring probability: %.2f%%", percentage));
            if (percentage >= 70) {
                response.append("🌟 High chances!");
            } else if (percentage >= 40) {
                response.append("💡 Moderate chances");
            } else {
                response.append("⚠️ Needs improvement");
            }

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
