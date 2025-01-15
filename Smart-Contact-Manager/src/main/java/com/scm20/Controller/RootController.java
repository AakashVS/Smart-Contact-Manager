package com.scm20.Controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm20.entities.User;
import com.scm20.helpers.Helper;
import com.scm20.services.Userservices;

@ControllerAdvice
public class RootController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());


    @Autowired
    private Userservices userservices;

     @ModelAttribute
    public void addLoggedUser(Model model ,Authentication authentication){

        if (authentication==null){
            return;
        }
        System.out.println("adding logged in user to the model");
        
        String username = Helper.getEmailOfLoggedInUser(authentication);

        logger.info("User logged in: {}",username);

        //get the user from database
        User user = userservices.getUserByEmail(username);
        System.out.println(user);  
        System.out.println(user.getEmail());
        System.out.println(user.getName());
        model.addAttribute("loggedInUser",user);

    }

    
}
