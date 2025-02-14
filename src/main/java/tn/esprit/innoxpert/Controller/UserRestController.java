package tn.esprit.innoxpert.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.innoxpert.Entity.User;
import tn.esprit.innoxpert.Service.UserService;
import tn.esprit.innoxpert.Service.UserServiceInterface;

import java.util.List;

@Tag(name = "User Management")
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserRestController {
    @Autowired
    UserServiceInterface UserService;

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers()
    {
        return UserService.getAllUsers();
    }
    @GetMapping("/getUserById/{idUser}")
    public User getUserById(@PathVariable("idUser") Long idUser)
    {
        return UserService.getUserById(idUser);
    }
    @PostMapping("/addUser")
    public User addUser ( @RequestBody User User)
    {
        return UserService.addUser(User);
    }

    @DeleteMapping("/deleteUser/{idUser}")
    public void deleteUserById(@PathVariable ("idUser") Long idUser)
    {
        UserService.removeUserById(idUser);
    }

    @PutMapping("/updateUser")

    public User updateUser(@RequestBody User User)
    {
        return UserService.updateUser(User);
    }
}
