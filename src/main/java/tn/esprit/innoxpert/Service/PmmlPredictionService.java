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
        PMML pmml = PMMLUtil.unmarshal(new FileInputStream("src/main/resources/hiring_predictor_model.pmml"));

        // Initialize model evaluator
        this.modelEvaluator = new ModelEvaluatorBuilder(pmml)
                .build();

        this.modelEvaluator.verify();

        // Debug: Print model information
        System.out.println("Model name: " + modelEvaluator.getModel().getModelName());
        System.out.println("Input fields:");
        for (InputField field : modelEvaluator.getInputFields()) {
            System.out.println(" - " + field.getName() + " (" + field.getDataType() + ")");
        }
    }

    public double predict(String option, String sujet, String entreprise) {
        Map<FieldName, FieldValue> input = new LinkedHashMap<>();

        // Prepare input values
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
                input.put(fieldName, inputField.prepare(value));
            }
        }

        // Perform the evaluation
        Map<FieldName, ?> results = modelEvaluator.evaluate(input);

        // Extract the prediction
        FieldName targetFieldName = modelEvaluator.getTargetFields().get(0).getName();
        Object targetValue = results.get(targetFieldName);

        // Handle different result types
        if (targetValue instanceof Computable) {
            targetValue = ((Computable) targetValue).getResult();
        }

        // Return numerical result or -1 for errors
        if (targetValue instanceof Number) {
            return ((Number) targetValue).doubleValue();
        } else if (targetValue instanceof String) {
            try {
                return Double.parseDouble((String) targetValue);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing prediction value: " + targetValue);
            }
        }

        return -1;
    }
}