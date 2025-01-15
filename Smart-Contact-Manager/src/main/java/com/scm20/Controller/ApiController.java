package com.scm20.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm20.entities.Contact;
import com.scm20.services.ContactService;

@RestController
@RequestMapping("/api")
public class ApiController {

    // get the all user
    
    @Autowired
    private ContactService contactService;


    @GetMapping("/contact/{contactId}")
    public Contact getContact(@PathVariable String contactId){

        return contactService.getById(contactId);
 
    }
    
}
