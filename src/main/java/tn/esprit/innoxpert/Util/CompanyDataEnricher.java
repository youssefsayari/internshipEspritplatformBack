package tn.esprit.innoxpert.Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tn.esprit.innoxpert.Entity.Company;
import tn.esprit.innoxpert.Entity.TypeSector;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
@Component  // Ajoutez cette annotation
public class CompanyDataEnricher {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private static final String PDL_ENRICH_URL = "https://api.peopledatalabs.com/v5/company/enrich";

    // Mapping des industries
    private static final Map<String, TypeSector> INDUSTRY_MAPPING = Map.ofEntries(
            Map.entry("technology", TypeSector.TECHNOLOGY),
            Map.entry("software", TypeSector.TECHNOLOGY),
            Map.entry("it", TypeSector.TECHNOLOGY),
            Map.entry("internet", TypeSector.TECHNOLOGY),
            Map.entry("computer", TypeSector.TECHNOLOGY),
            Map.entry("finance", TypeSector.FINANCE),
            Map.entry("banking", TypeSector.FINANCE),
            Map.entry("insurance", TypeSector.FINANCE),
            Map.entry("healthcare", TypeSector.HEALTHCARE),
            Map.entry("medical", TypeSector.HEALTHCARE),
            Map.entry("pharmaceutical", TypeSector.HEALTHCARE),
            Map.entry("education", TypeSector.EDUCATION)
    );

    public CompanyDataEnricher(RestTemplate restTemplate,
                               @Value("${pdl.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public Company enrichCompanyData(String name, String website) throws Exception {
        // Validation des paramètres
        if ((name == null || name.isBlank()) && (website == null || website.isBlank())) {
            throw new IllegalArgumentException("Au moins un paramètre (name ou website) est requis");
        }

        // Appel API
        PdlEnrichResponse pdlCompany = callPeopleDataLabsApi(name, website);

        // Transformation de la réponse
        return mapToCompanyEntity(pdlCompany);
    }

    private PdlEnrichResponse callPeopleDataLabsApi(String name, String website) throws Exception {
        String url = String.format("%s?pretty=false&website=%s&name=%s",
                PDL_ENRICH_URL,
                website != null ? website : "",
                name != null ? name : "");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<PdlEnrichResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                PdlEnrichResponse.class);

        return response.getBody();
    }

    private Company mapToCompanyEntity(PdlEnrichResponse pdlCompany) {
        Company company = new Company();

        // Infos de base
        company.setName(pdlCompany.name != null ? pdlCompany.name : "");
        company.setWebsite(formatWebsiteUrl(pdlCompany.website));
        company.setEmail(generateCompanyEmail(pdlCompany));

        // Adresse
        company.setAddress(formatAddress(pdlCompany.location));

        // Secteur
        company.setSector(determineSector(pdlCompany));

        // Téléphone
        company.setPhone(formatPhoneNumber(pdlCompany.phone));

        // Autres champs
        company.setAbbreviation(generateAbbreviation(pdlCompany.name));
        company.setFoundingYear(parseFoundedDate(pdlCompany.founded));
        company.setFounders(formatFounders(pdlCompany));
        company.setLabelDate(new Date());
        company.setSecretKey(generateRandomSecretKey());

        return company;
    }
    private Date parseFoundedDate(Integer foundedYear) {
        if (foundedYear == null || foundedYear < 1900 || foundedYear > LocalDate.now().getYear()) {
            return null;
        }

        return Date.from(
                LocalDate.of(foundedYear, 1, 1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    private String formatWebsiteUrl(String website) {
        return website != null ?
                "https://" + website.replaceFirst("^(https?://)?", "") : "";
    }

    private String generateCompanyEmail(PdlEnrichResponse pdlCompany) {
        return pdlCompany.email != null ? pdlCompany.email :
                "contact@" + (pdlCompany.website != null ? pdlCompany.website :
                        pdlCompany.name.toLowerCase().replace(" ", "") + ".com");
    }

    private String formatAddress(PdlLocation location) {
        if (location == null) return "Adresse non disponible";

        return String.join(", ",
                location.street_address != null ? location.street_address : "",
                location.locality != null ? location.locality : "",
                location.region != null ? location.region : "",
                location.country != null ? location.country : ""
        ).replaceAll("^\\s*,\\s*|\\s*,\\s*$", "");
    }

    private TypeSector determineSector(PdlEnrichResponse pdlCompany) {
        // 1. Vérifier l'industrie principale
        if (pdlCompany.industry != null) {
            String industryLower = pdlCompany.industry.toLowerCase();
            for (Map.Entry<String, TypeSector> entry : INDUSTRY_MAPPING.entrySet()) {
                if (industryLower.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }

        // 2. Vérifier les tags
        if (pdlCompany.tags != null) {
            for (String tag : pdlCompany.tags) {
                String tagLower = tag.toLowerCase();
                if (tagLower.contains("tech") || tagLower.contains("software")) {
                    return TypeSector.TECHNOLOGY;
                }
                if (tagLower.contains("finance") || tagLower.contains("bank")) {
                    return TypeSector.FINANCE;
                }
                if (tagLower.contains("health") || tagLower.contains("medical")) {
                    return TypeSector.HEALTHCARE;
                }
                if (tagLower.contains("educ") || tagLower.contains("school")) {
                    return TypeSector.EDUCATION;
                }
            }
        }

        return TypeSector.OTHER;
    }

    private Long formatPhoneNumber(String phone) {
        try {
            return phone != null ?
                    Long.parseLong(phone.replaceAll("[^0-9]", "")) : 0L;
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private String generateAbbreviation(String name) {
        if (name == null || name.isBlank()) return "CMP";

        return Arrays.stream(name.split(" "))
                .filter(word -> !word.isEmpty())
                .limit(3)
                .map(word -> word.substring(0, 1))
                .collect(Collectors.joining())
                .toUpperCase();
    }

    private String formatFounders(PdlEnrichResponse pdlCompany) {
        if (pdlCompany.founders != null && !pdlCompany.founders.isEmpty()) {
            return String.join(", ", pdlCompany.founders);
        }
        if (pdlCompany.alternative_names != null && !pdlCompany.alternative_names.isEmpty()) {
            return "Fondateurs inconnus (" + pdlCompany.alternative_names.get(0) + ")";
        }
        return "Fondateurs non spécifiés";
    }

    private String generateRandomSecretKey() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        String special = "!@#$&*";
        String all = upper + lower + digits + special;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Au moins une majuscule, un caractère spécial et un chiffre
        sb.append(upper.charAt(random.nextInt(upper.length())));
        sb.append(special.charAt(random.nextInt(special.length())));
        sb.append(digits.charAt(random.nextInt(digits.length())));

        // Remplir le reste
        for (int i = 0; i < 5; i++) {
            sb.append(all.charAt(random.nextInt(all.length())));
        }

        // Mélanger
        char[] array = sb.toString().toCharArray();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        return new String(array);
    }

    // Classes internes pour le parsing JSON
    private static class PdlEnrichResponse {
        public String name;
        public String website;
        public String email;
        public String phone;
        public String industry;
        public Integer founded;
        public List<String> founders;
        public List<String> tags;
        public List<String> alternative_names;
        public PdlLocation location;
    }

    private static class PdlLocation {
        public String street_address;
        public String locality;
        public String region;
        public String country;
    }
}