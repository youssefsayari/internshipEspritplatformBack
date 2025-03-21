package tn.esprit.innoxpert.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.innoxpert.Service.SendEmailService;

@RestController
public class EmailController {
    @Autowired
    private SendEmailService sendEmailService;
    @GetMapping("sendEmail")
    public String sendEmail() {
       // sendEmailService.sendEmail("youssef.sayari@esprit.tn","Test Body","Test Subject");
        return "Email Sent";
    }
}
