package com.scm20.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication){


        // if we are loggin email or password then how to get the email and password
        if ( authentication instanceof OAuth2AuthenticationToken) 
        {

            var OAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;

            var clientId = OAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();

            String username="";

            if(clientId.equalsIgnoreCase("google")){
             // sign with google 
                System.out.println("get the email from google");
               username= oauth2User.getAttribute("email").toString();

            }
            
            else if(clientId.equalsIgnoreCase("github")){
                
               // sign with github
                System.out.println("get the email from github"); 
                username = oauth2User.getAttribute("email") != null ? 
                oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString()+"@gmail.com";

            }



        return username;
        }
        else{
            System.out.println("getting data from  local DB");
            return authentication.getName();

        }
 

    }



    public static String getlinkforEmailVerification(String emailToken){

        String link ="http://localhost:8082/auth/verify-email?token=" +emailToken;

        return link;

    }
    
}
