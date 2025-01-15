package com.scm20.services;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {

    String uploadImage(MultipartFile profileimage,String filename);

    String getURLFromPublicID(String publicId);
    
}
