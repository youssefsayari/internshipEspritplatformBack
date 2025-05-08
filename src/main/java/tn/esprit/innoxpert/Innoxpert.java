package tn.esprit.innoxpert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync

public class Innoxpert {

	public static void main(String[] args) {
		SpringApplication.run(Innoxpert.class, args);

		//new EmailClass().sendMeetingReminder("kthiri1919@gmail.com", "John Doe", "Jane Smith", "2024-03-15", "14:30", "https://meet.jit.si/MyMeeting");
		//new EmailClass().sendEmail("kthiri.amenallah02@gmail.com","HELLICH EL HELIIICHI","YAHELIICHHHH");
		/*try {
			// Remplace ce chemin par le tien !
			String pythonScriptPath = "src/main/resources/train_model.py";

			// Commande à exécuter
			ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath);

			pb.redirectErrorStream(true);
			Process process = pb.start();

			// Lire la sortie console du script
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				System.out.println("[PYTHON] " + line);
			}

			int exitCode = process.waitFor();
			System.out.println("Script Python terminé avec code de sortie : " + exitCode);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		 */


		/*try {
			XGBoostPredictionService predictor = new XGBoostPredictionService();
			double result = predictor.predict("SAE", "Blockchain", "Vermeg");

			// Afficher le pourcentage
			if(result >= 0 && result <= 1) {
				double percentage = result * 100;
				System.out.printf("✅ Pourcentage de chance d'embauche : %.2f%%\n", percentage);

				// Interprétation supplémentaire
				if(percentage >= 70) {
					System.out.println("🌟 Très bonnes chances !");
				} else if(percentage >= 40) {
					System.out.println("💡 Chances modérées");
				} else {
					System.out.println("⚠️ Besoin d'amélioration");
				}
			} else {
				System.out.println("❌ Erreur de prédiction");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	}




