package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.DTO.JwtRequest;
import tn.esprit.innoxpert.DTO.UserResponse;
import tn.esprit.innoxpert.DTO.UserRole;
import tn.esprit.innoxpert.Entity.TypeUser;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Exceptions.NotFoundException;
import tn.esprit.innoxpert.Service.UserServiceInterface;
import tn.esprit.innoxpert.Util.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "User Management")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserRestController {
    @Autowired
    UserServiceInterface userservice;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest) {
        if (jwtRequest.getIdentifiant() == null || jwtRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing identifiant or password");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getIdentifiant(), jwtRequest.getPassword())
            );

            String jwtToken = jwtUtil.generateToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Map<String, Object> response = new HashMap<>();
          //  response.put("identifiant",jwtRequest.getIdentifiant());
            response.put("token", jwtToken);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed: Invalid identifiant or password");
        }
    }
  /*  @PostMapping("/me")
    public ResponseEntity<?> getConnectedUser(@RequestBody String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing token in request body");
        }

        try {
            // Trim token in case of extra spaces or newlines
            token = token.trim();

            // Extract identifiant from the token
            String identifiant = jwtUtil.extractIdentifiant(token);

            // Fetch the user from the database
            User user = userservice.getUserByIdentifiant(identifiant);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
            }

            // Return user details
            Map<String, Object> response = new HashMap<>();
            response.put("idUser", user.getIdUser());
            response.put("identifiant", user.getIdentifiant());
            response.put("email", user.getEmail());
            response.put("telephone", user.getTelephone());
            response.put("typeUser", user.getTypeUser().name());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token: " + e.getMessage());
        }
    }*/
  /*@PostMapping("/decodeToken")
  public ResponseEntity<?> decodeToken(@RequestBody String token) {
      try {
          String identifiant = userservice.decodeJwtToken(token);

          Map<String, Object> response = new HashMap<>();
          response.put("identifiant", identifiant);


          return ResponseEntity.ok(response);
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token: " + e.getMessage());
      }
  }
*/

//    @PostMapping("/decode-token")
//    public ResponseEntity<?> decodeToken(@RequestBody String token) {
//        try {
//            Map<String, Object> decodedPayload = userservice.decodeJwtWithoutVerification(token);
//            return ResponseEntity.ok(decodedPayload);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token: " + e.getMessage());
//        }
//    }


    @PostMapping("/decode-token")
    public ResponseEntity<?> decodeToken(@RequestBody String token) {
        try {
            String identifiant = userservice.extractIdentifiantFromJwt(token);
            User user = userservice.getUserByIdentifiant(identifiant);

            // Log pour vérifier l'objet renvoyé
            System.out.println("Decoded user: " + user.getIdUser());

            return ResponseEntity.ok(user);  // Assurez-vous que l'objet User contient bien 'idUser'
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token: " + e.getMessage());
        }
    }


    @PostMapping("/decode-token-Role")
    public ResponseEntity<?> decodeTokenRole(@RequestBody String token) {
        try {
            String classe = "";
            String identifiant = userservice.extractIdentifiantFromJwt(token);
            User user = userservice.getUserByIdentifiant(identifiant);
            String role = String.valueOf(user.getTypeUser());
            if (role.equals("Company")) {
                classe = String.valueOf(user.getIdUser());
            }else{
                classe = user.getClasse();
            }
            Long id= user.getIdUser();
            String email = user.getEmail();
            UserRole userRole = new UserRole(role, classe, id, email);
            return ResponseEntity.ok(userRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token: " + e.getMessage());
        }
    }

    @PostMapping("/affectation/{userId}/{tutorId}")
    public ResponseEntity<?> affectationTutor(@PathVariable Long userId, @PathVariable Long tutorId) {
        userservice.affectationTutor(userId, tutorId);
        return ResponseEntity.ok("Tutor affected successfully");
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers()
    {
        return userservice.getAllUsers();
    }

    @GetMapping("/getUserBytypeUser")
    public ResponseEntity<List<UserResponse>> getUserBytypeUser(@RequestParam String typeUser) {
        try {
            List<UserResponse> users = userservice.getUserBytypeUser(typeUser);
            return ResponseEntity.ok(users);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
    @PutMapping("/updateAdd/{userId}")
    public ResponseEntity<String> updateTutorAdd(@PathVariable Long userId, @RequestParam String key) {
        try {
            userservice.updateTutorAdd(key, userId);
            return ResponseEntity.ok("Tutor updated successfully");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error updating tutor: " + e.getMessage());
        }
    }

    @PutMapping("/updateRem/{userId}")
    public ResponseEntity<String> updateTutorRem(@PathVariable Long userId, @RequestParam String key) {
        try {
            userservice.updateTutorRem(key, userId);
            return ResponseEntity.ok("Tutor updated successfully");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(500).body("Error updating tutor: " + e.getMessage());
        }
    }

    @GetMapping("/getUserById/{idUser}")
    public User getUserById(@PathVariable("idUser") Long idUser)
    {
        return userservice.getUserById(idUser);
    }
    @PostMapping("/addUser")
    public User addUser ( @RequestBody User User)
    {
        return userservice.addUser(User);
    }

    @DeleteMapping("/deleteUser/{idUser}")
    public void deleteUserById(@PathVariable ("idUser") Long idUser)
    {
        userservice.removeUserById(idUser);
    }

    @PutMapping("/updateUser")

    public User updateUser(@RequestBody User User)
    {
        return userservice.updateUser(User);
    }
    /*SAYAAAAAAAAAAARIIIIIIIIIIIIIII*/
    @GetMapping("/getTypeUser/{idUser}")
    public TypeUser getUserType(@PathVariable Long idUser) {
        return userservice.getUserType(idUser);
    }
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@RequestBody String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email is required"));
        }

        String responseMessage = userservice.generateOtp(email);

        return ResponseEntity.ok(Map.of("message", responseMessage));
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<Boolean> verifyOtp(@RequestParam String email,@RequestParam Long otp) {
        boolean response = userservice.validateOtp(email,otp);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestParam String email, @RequestParam String newPassword) {
        boolean isChanged = userservice.changePassword(email, newPassword);

        Map<String, String> response = new HashMap<>();
        if (isChanged) {
            response.put("message", "Password successfully changed.");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "User not found.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


}
