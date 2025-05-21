package tn.esprit.innoxpert.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Service.UserServiceInterface;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserServiceInterface userService) {
        return args -> {
            for (TypeUser type : TypeUser.values()) {
                String roleName = type.name();

                // Check if user already exists to avoid duplicates
                if (userService.getUserByIdentifiant(roleName) == null) {
                    User user = new User();
                    user.setFirstName(roleName);
                    user.setLastName("Default");
                    user.setIdentifiant(roleName);
                    user.setPassword(roleName); // will be encoded by service
                    user.setEmail(roleName.toLowerCase() + "@example.com");
                    user.setTelephone(123456789L);
                    user.setClasse("DefaultClasse");
                    user.setTypeUser(type);

                    userService.addUser(user);
                    System.out.println("Created user for role: " + roleName);
                } else {
                    System.out.println("User already exists for role: " + roleName);
                }
            }
        };
    }
}
