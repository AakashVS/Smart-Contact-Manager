package com.scm20.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm20.entities.User;
import com.scm20.helpers.Message;
import com.scm20.helpers.MessageType;
import com.scm20.repositories.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

  
    @Autowired
    private UserRepo userRepo;

         // verify email
        @GetMapping("/verify-email")
        public String verifyEmail(@RequestParam("token") String token,HttpSession session){
       
        System.out.println("verify Email");
        User user = userRepo.findByEmailToken(token).orElse(null);
        
        if (user!= null) {

            //user ko fetch karna hai aur process karna hai
            if (user.getEmailToken().equals(token)) {
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepo.save(user);

                session.setAttribute("message", Message.builder()
                .type(MessageType.green)
                .content(" Your email is verified !! Now you can login.")
                .build());
                return "successPage";
            }
            session.setAttribute("message", Message.builder()
            .type(MessageType.red)
            .content("Email is not verified !! Token is not associated with user.")
            .build());
            return "errorPage";    
        }

        session.setAttribute("message", Message.builder()
        .type(MessageType.red)
        .content("Email is not verified !! Token is not associated with user.")
        .build());
        return "errorPage";
    }
}
