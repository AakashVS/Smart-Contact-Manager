package com.scm20.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.scm20.entities.User;
import com.scm20.helpers.AppConstants;
import com.scm20.helpers.Helper;
import com.scm20.helpers.ResourceNotFoundException;
import com.scm20.repositories.UserRepo;
import com.scm20.services.EmailService;
import com.scm20.services.Userservices;



@Service
public class UserServiceImpl implements Userservices{
    
    @Autowired
    public  UserRepo userRepo;
     
    @Autowired
    public EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger=LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        // userid  have to generate  dynamically
        String userId=UUID.randomUUID().toString();
        user.setUserId(userId);
        //password encode
        //user.setPassword(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // set the user role
       user.setRoleList(List.of(AppConstants.ROLE_USER));
       logger.info(user.getProvider().toString());
       
       // token generating for email verification
       String emailToken = UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
       //save user in database
       User savedUser = userRepo.save(user);
       //generate email link
       String emailLink = Helper.getlinkforEmailVerification(emailToken);
       emailService.sendEmail(savedUser.getEmail(), "Verify Account :Smart Contact Manager", emailLink);
       return savedUser;
       
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepo.findById(user.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());
        user2.setAbout(user.getProviderUserId());

        // save the user in our database
        User save = userRepo.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepo.delete(user2);
      
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2 = userRepo.findById(userId).orElse(null);
        return user2 !=null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {

        User user = userRepo.findById(email).orElse(null);
        return user !=null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
       
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }
 
   
    
}
