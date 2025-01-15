package com.scm20.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm20.entities.User;
import com.scm20.forms.UserForm;
import com.scm20.helpers.Message;
import com.scm20.helpers.MessageType;
import com.scm20.services.Userservices;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


@Controller
public class PageController {

    @Autowired
    private Userservices userservices;
    
    @RequestMapping("/")
    private String index(){
        return "redirect:/home";
    }
	

    @RequestMapping("/home")
    public String home(Model model)
    {
      
        model.addAttribute("name", "Substring Technologies");
        model.addAttribute("youTubeChannel", "Learn Code  With Aakash");
        model.addAttribute("gitRepo", "https://github.com/AakashVS");
        return "home";

    }
    // about route
     @RequestMapping("/about")
    public String aboutPage(Model model){
        model.addAttribute("isLogin", true);
        System.out.println("this is about page");
        return "about";
    }

    //services 
    @RequestMapping("/service")
    public String servicesPage(){
        System.out.println("this is services");
        return "services";
    }
    //contact
    @GetMapping("/contact")
    public String contact (){
        return new String("contact");

    }
    //this is  showing login page 
    @GetMapping("/login")
    public String login (){
        return new String("login");

    }
    //registration page
    @GetMapping("/signup")
    public String signupForm(Model model) {

        //using bydefault set the properties
        UserForm userForm = new UserForm();
        // userForm.setName("akash");
        // userForm.setEmail("akashshirture@gmail.com");
        // userForm.setPassword("akash@14");
        // userForm.setPhoneNumber("9340329059");
        // userForm.setAbout("hiiiiii akash");
        model.addAttribute("userForm",userForm);
        return "signup"; 
       }
       
       //processing register
       @RequestMapping(value = "/do-signup",method = RequestMethod.POST)
       public String processSignUp(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,HttpSession session){
        System.out.println("processing singup");
        //fetch form data
        //UserForm
        System.out.println(userForm);

        //validate form data
        if(rBindingResult.hasErrors()){
            return "signup";
        }

        //userService

        // UserForm is convert to User
        //using builder method
    //     User user = User.builder()
    //     .name(userForm.getName())
    //     .email(userForm.getEmail())
    //     .password(userForm.getPassword())
    //     .about(userForm.getAbout())
    //     .phoneNumber(userForm.getPhoneNumber())
    //     .profilePic("https://as1.ftcdn.net/v2/jpg/05/16/27/60/1000_F_516276029_aMcP4HU81RVrYX8f5qCAOCCuOiCsu5UF.jpg")
    //     .build();
    //     User saveUser = userservices.saveUser(user);
    //   System.out.println("save user");
        
    // set the properties using object with set and get method
    User user = new User();
    user.setName(userForm.getName());
    user.setEmail(userForm.getEmail());
    user.setPassword(userForm.getPassword());
    user.setAbout(userForm.getAbout());
    user.setPhoneNumber(userForm.getPhoneNumber());
    user.setEnabled(false);
    user.setProfilePic("https://as1.ftcdn.net/v2/jpg/05/16/27/60/1000_F_516276029_aMcP4HU81RVrYX8f5qCAOCCuOiCsu5UF.jpg");
    User saveUser = userservices.saveUser(user);

    System.out.println("user saved :");
    
    //add mesaage
    Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
    session.setAttribute("message",message);

        // redirect to login page
        return "redirect:/signup";

       }

}
