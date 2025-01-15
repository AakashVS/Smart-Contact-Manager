package com.scm20.Controller;


import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm20.entities.Contact;
import com.scm20.entities.User;
import com.scm20.forms.ContactForm;
import com.scm20.forms.ContactSearchForm;
import com.scm20.helpers.AppConstants;
import com.scm20.helpers.Helper;
import com.scm20.helpers.Message;
import com.scm20.helpers.MessageType;
import com.scm20.services.ContactService;
import com.scm20.services.ProfileService;
import com.scm20.services.Userservices;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("user/contact")
public class ContactController {

     private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);
   
    @Autowired
    private ContactService contactService;

    @Autowired
    private Userservices userservices;
    
    @Autowired
    private ProfileService profileService;
   

    // add to contact page user:handler
    @RequestMapping("/add")
    public String addContactview(Model model){
         ContactForm contactForm = new ContactForm();
         contactForm.setFavorite(true);
         model.addAttribute("contactForm", contactForm);
         return "user/addcontact";
    }

      @RequestMapping(value = "/add",method = RequestMethod.POST)
      public String addContact ( @Valid @ModelAttribute ContactForm conatctForm,BindingResult result,Authentication authentication,HttpSession session ){

     //validation form
     if(result.hasErrors()){
        result.getAllErrors().forEach(error-> logger.info(error.toString()));
        session.setAttribute("message", Message.builder()
        .content("please correct the following error")
        .type(MessageType.red)
        .build());
        return"user/addcontact"; 
     }

     // process the form  data
     String userName = Helper.getEmailOfLoggedInUser(authentication);

     //form --contact
     User user = userservices.getUserByEmail(userName);
    
     //process image
     logger.info("file details: {}",conatctForm.getProfileimage().getOriginalFilename());

     Contact contact = new Contact();
     contact.setName(conatctForm.getName());
     contact.setEmail(conatctForm.getEmail());
     contact.setFavorite(conatctForm.isFavorite());
     contact.setAddress(conatctForm.getAddress());
     contact.setDescription(conatctForm.getDescription());
     contact.setPhoneNumber(conatctForm.getPhoneNumber());
     contact.setLinkedinLink(conatctForm.getLinkedLink());
     contact.setWebsiteLink(conatctForm.getWebsiteLink());
     contact.setUser(user);

     if (conatctForm.getProfileimage() != null && !conatctForm.getProfileimage().isEmpty()) {
      String filename = UUID.randomUUID().toString();
      // file uploading
      String fileURL = profileService.uploadImage(conatctForm.getProfileimage(),filename);
      contact.setPicture(fileURL);
      contact.setCloudinaryImagePublicId(filename);
     }
    contactService.save(contact);  
    System.out.println(conatctForm);

    session.setAttribute("message", new Message("Contact successfully added!", MessageType.green));
    return "redirect:/user/contact/add";

    }

    // views of contacts
    @RequestMapping
    public String viewContact(
    @RequestParam (value = "page",defaultValue = "0") int page,
    @RequestParam (value = "size",defaultValue=AppConstants.PAGE_SIZE+"") int size,
    @RequestParam (value = "sortBy",defaultValue = "name") String sortBy,
    @RequestParam (value = "direction",defaultValue = "asc") String direction, 
    Model model,
    Authentication authentication){
   
     //load all user contacts
     String username = Helper.getEmailOfLoggedInUser(authentication);
     User user = userservices.getUserByEmail(username);
     Page <Contact> Pagecontact = contactService.getByUser(user,page,size,sortBy,direction);

     model.addAttribute("Pagecontact", Pagecontact);
     model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
     model.addAttribute("contactSearchForm", new ContactSearchForm());
     return "user/contact";
    }

   // search handler
   @RequestMapping("/search")
   public String searchHandler(
    @ModelAttribute ContactSearchForm contactSearchForm,
    @RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE +"") int size,
    @RequestParam(value = "page",defaultValue = "0")int page,
    @RequestParam(value = "sortBy",defaultValue = "name")String sortBy,
    @RequestParam(value = "direction",defaultValue = "asc") String direction,
    Model model ,Authentication authentication
   ){
    logger.info("field: {}, keyword: {}",contactSearchForm.getField(),contactSearchForm.getValue());

    var user = userservices.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));


    Page<Contact> Pagecontact=null;
    if(contactSearchForm.getField().equalsIgnoreCase("name")){
        Pagecontact  = contactService.searchByName( contactSearchForm.getValue(),size, page, sortBy, direction,user);
    }
    else if(contactSearchForm.getField().equalsIgnoreCase("email")){
        Pagecontact  = contactService.searchByEmail(contactSearchForm.getValue(),size, page, sortBy, direction,user);
    }
    else if(contactSearchForm.getField().equalsIgnoreCase("phone")){
        Pagecontact  = contactService.searchByPhoneNumber(contactSearchForm.getValue(),size, page, sortBy, direction,user);
    }
    logger.info("Pagecontact {}", Pagecontact);
    model.addAttribute("Pagecontact",   Pagecontact);
    model.addAttribute("contactSerachForm ", contactSearchForm);
    model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
     return"user/search";
   }

   // delete contact handler

   @RequestMapping("/delete/{contactId}")
   public String deleteContact(
    @PathVariable ("contactId") String contactId,
    HttpSession session
    ){

    contactService.delete(contactId);

    logger.info("contactId {} deleted",contactId);

        session.setAttribute("message", 
          Message.builder()
         .content("Contact is deleted succesfully !")
         .type(MessageType.green)
         .build());

    return "redirect:/user/contact";
   }

   // update handler(view) on contact page
   @GetMapping("/update/{contactId}")
   public String updateContactFormView(
    @PathVariable ("contactId") String contactId,Model model
   ){

       var contact = contactService.getById(contactId);

       ContactForm contactForm = new ContactForm();
       
       contactForm.setName(contact.getName());
       contactForm.setEmail(contact.getEmail());
       contactForm.setPhoneNumber(contact.getPhoneNumber());
       contactForm.setAddress(contact.getAddress());
       contactForm.setDescription(contact.getDescription());
       contactForm.setFavorite(contact.isFavorite());
       contactForm.setDescription(contact.getDescription());
       contactForm.setWebsiteLink(contact.getWebsiteLink());
       contactForm.setPicture(contact.getPicture());
       contactForm.setLinkedLink(contact.getLinkedinLink());

       model.addAttribute("contactForm", contactForm);
       model.addAttribute("contactId", contactId);
  
    return "user/update_contact";
   }
   

   // update contact from view side
   @RequestMapping(value = "/update/{contactId}",method = RequestMethod.POST)
   public String updateContact(@PathVariable("contactId") String contactId,@Valid
    @ModelAttribute ContactForm contactForm,BindingResult bindingResult ,Model model){
      
    // update contact
    if (bindingResult.hasErrors()) {
      return "user/update_contact";
      
    }

    var cont = contactService.getById(contactId);
    cont.setId(contactId);
    cont.setName(contactForm.getName());
    cont.setEmail(contactForm.getEmail());
    cont.setPhoneNumber(contactForm.getPhoneNumber());
    cont.setAddress(contactForm.getAddress());
    cont.setDescription(contactForm.getDescription());
    cont.setFavorite(contactForm.isFavorite());
    cont.setWebsiteLink(contactForm.getWebsiteLink());
    cont.setLinkedinLink(contactForm.getLinkedLink());
    
    //process image:
    if (contactForm.getProfileimage() !=null && !contactForm.getProfileimage().isEmpty()) {

     logger.info("file is not empty"); 
    String fileName = UUID.randomUUID().toString();
    String  profileURL = profileService.uploadImage(contactForm.getProfileimage(), fileName);
    cont.setCloudinaryImagePublicId(fileName);
    cont.setPicture(profileURL);
    contactForm.setPicture(profileURL); 
    }  else{
      logger.info("file is emptys");
    }

    var updateConst = contactService.update(cont);

    logger.info("update contact {}",updateConst);
    model.addAttribute("message", Message.builder().content("Contact Updated").type(MessageType.green).build());


    return "redirect:/user/contact/update/" +contactId;

   }



    
}
