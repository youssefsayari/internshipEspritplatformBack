package tn.esprit.innoxpert.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import tn.esprit.innoxpert.Repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tn.esprit.innoxpert.Util.CompanyDataEnricher;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    public ApplicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByIdentifiant(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    /*sayari : pour api auto form add company completion*/
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public CompanyDataEnricher companyDataEnricher(
            RestTemplate restTemplate,
            @Value("${pdl.api.key}") String pdlApiKey) {
        return new CompanyDataEnricher(restTemplate, pdlApiKey);
    }
    /* END sayari : pour api auto form add company completion*/
}
