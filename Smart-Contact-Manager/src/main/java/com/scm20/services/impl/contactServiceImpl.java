package com.scm20.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm20.entities.Contact;
import com.scm20.entities.User;
import com.scm20.helpers.ResourceNotFoundException;
import com.scm20.repositories.ContactRepo;
import com.scm20.services.ContactService;


@Service
public class contactServiceImpl implements ContactService {
    
    @Autowired
    private ContactRepo contactRepo; 

    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        var contactOld = contactRepo.findById(contact.getId())
        .orElseThrow(()-> new ResourceNotFoundException("Contact is not found")); 

        contactOld.setName(contact.getName());
        contactOld.setEmail(contact.getEmail());
        contactOld.setPhoneNumber(contact.getPhoneNumber());
        contactOld.setAddress(contact.getAddress());
        contactOld.setDescription(contact.getDescription());
        contactOld.setPicture(contact.getPicture());
        contactOld.setFavorite(contact.isFavorite());
        contactOld.setWebsiteLink(contact.getWebsiteLink());
        contactOld.setLinkedinLink(contact.getLinkedinLink());
        contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepo.save(contactOld);

    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
       
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("contact is not found with this is id" +id));
       
    }

    @Override
    public void delete(String id) {
        var conatct =  contactRepo.findById(id)
           .orElseThrow(()-> new ResourceNotFoundException("contact is not found with this is id" +id));
        contactRepo.delete(conatct);
    }
  
    @Override
    public List<Contact> getByUserId(String userId) {
        // Fetch contacts by user ID from repository
        return contactRepo.findByUserId(userId);
    }
    @Override
    public Page <Contact> getByUser(User user,int page,int size,String sortBy,String direction) {
        // Fetch contacts by user object from repository
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size,sort);
        return contactRepo.findByUser(user,pageable);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int size, int page, String sortBy, String order,User user ) {
       Sort sort=order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
       var pageable=PageRequest.of(page,size,sort);
        return contactRepo.findByUserAndNameContaining( user,nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order,User user ) {
        Sort sort=order.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
        var pageable=PageRequest.of(page,size,sort);
        return contactRepo.findByUserAndEmailContaining(user, emailKeyword,pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int size, int page, String sortBy,
               String order ,User user) {
                Sort sort=order.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
                var pageable=PageRequest.of(page,size,sort);
                 return contactRepo.findByUserAndPhoneNumberContaining(user,phoneNumberKeyword, pageable);
    }

   

    
}
