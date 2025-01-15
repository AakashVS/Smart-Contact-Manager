package com.scm20.Controller;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class UserController {
   
    @RequestMapping("/dashboard")
    public String userDashboard() {
        System.out.println("User dashboard accessed");
        return "user/dashboard";
    }

    @RequestMapping("/profile")
    public String userProfile(Model model, Authentication authentication) {
        return "user/profile"; 
    
    }
}
