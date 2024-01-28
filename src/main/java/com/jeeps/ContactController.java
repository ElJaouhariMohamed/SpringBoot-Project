package com.jeeps;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Properties;

@Controller
public class ContactController {
  
    @PostMapping("/sendEmail")
    public String contact(@RequestParam("name") String name,
                          @RequestParam("email") String email,
                          @RequestParam("socialStatus") String socialStatus,
                          @RequestParam("location") String location,
                          @RequestParam("subject") String subject,
                          @RequestParam("message") String message,
                          Model model) {

        String to = "elamranilyas@gmail.com"; // Email address to send the message to

        // Set up properties for the mail session
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Set up the mail session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("user_name", "password");
            }
        });

        try {
            // Create a new message
            Message mailMessage = new MimeMessage(session);
            mailMessage.setFrom(new InternetAddress(email));
            mailMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mailMessage.setSubject(subject);

            // Set the message body
            String messageBody = "Name: " + name + "\n" +
                                 "Email: " + email + "\n" +
                                 "SocialStatus: " + socialStatus + "\n" +
                                 "Address: " + location + "\n\n" +
                                 "Message:\n" + message;
            mailMessage.setText(messageBody);

            // Send the message
            Transport.send(mailMessage);

            // Add a success message to the model
            model.addAttribute("status", new status(200,"Message envoyé"));

        } catch (MessagingException e) {
            // Add an error message to the model
            model.addAttribute("status", new status(500,"Une erreur s'est produite, essayer ultérieurement!"));
        }

        // Return the view name
        return "redirect:/public/Contact";
    }
}

