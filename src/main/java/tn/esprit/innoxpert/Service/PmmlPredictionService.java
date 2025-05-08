package tn.esprit.innoxpert.Service;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class PmmlPredictionService {

    private final ModelEvaluator<?> modelEvaluator;

    public PmmlPredictionService() throws Exception {
        // Charger le fichier PMML
        PMML pmml = PMMLUtil.unmarshal(new FileInputStream("src/main/resources/hiring_predictor_model.pmml"));

        // Construire l'évaluateur
        ModelEvaluatorBuilder builder = new ModelEvaluatorBuilder(pmml);
        this.modelEvaluator = builder.build();

        ((ModelEvaluator<?>) this.modelEvaluator).verify();

        // Debug : afficher les champs
        System.out.println("Input fields:");
        for (InputField field : modelEvaluator.getInputFields()) {
            System.out.println(" - " + field.getName() + " (" + field.getDataType() + ")");
        }
    }

    public double predict(String option, String sujet, String entreprise) {
        option = option != null ? option.toUpperCase() : null;
        sujet = sujet != null ? sujet.toUpperCase() : null;
        entreprise = entreprise != null ? entreprise.toUpperCase() : null;

        Map<FieldName, FieldValue> input = new LinkedHashMap<>();

        for (InputField inputField : modelEvaluator.getInputFields()) {
            FieldName fieldName = inputField.getName();
            String fieldNameStr = fieldName.getValue();

            Object value = switch (fieldNameStr) {
                case "Option" -> option;
                case "Internship Subject" -> sujet;
                case "Company" -> entreprise;
                default -> null;
            };

            if (value != null) {
                FieldValue fieldValue = inputField.prepare(value);
                input.put(fieldName, fieldValue);
            }
        }

        Map<FieldName, ?> results = modelEvaluator.evaluate(input);

        // Récupère la probabilité de la classe 1 (Accepted = 1)
        FieldName probFieldName = FieldName.create("probability(1)");
        Object targetValue = results.get(probFieldName);

        if (targetValue instanceof Computable) {
            targetValue = ((Computable) targetValue).getResult();
        }

        if (targetValue instanceof Number) {
            return Math.max(0, Math.min(1, ((Number) targetValue).doubleValue()));
        } else if (targetValue instanceof String) {
            try {
                return Math.max(0, Math.min(1, Double.parseDouble((String) targetValue)));
            } catch (NumberFormatException e) {
                System.err.println("Erreur de parsing de la prédiction : " + targetValue);
            }
        }

        return -1;
    }
}
