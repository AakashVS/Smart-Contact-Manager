package com.scm20.services.impl;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm20.helpers.AppConstants;
import com.scm20.services.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

    private  Cloudinary cloudinary;
   
    @Autowired
    public ProfileServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile profileimage,String filename) {

        //String filename = UUID.randomUUID().toString();

        try {
            byte[] data = new byte[profileimage.getInputStream().available()];
            profileimage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id",filename));
           
            return this.getURLFromPublicID(filename);

        } catch (IOException e) {
            
            e.printStackTrace();
            
            return null;
        }   
    }


    @Override
    public String getURLFromPublicID(String publicId) {
     
    return cloudinary
    .url()
    .transformation(
        new Transformation<>()
        .width(AppConstants.PROFILE_IMAGE_WIDTH)
        .height(AppConstants.PROFILE_IMAGE_HEIGHT)
        .crop(AppConstants.PROFILE_IMAGE_CROP)
    )
    .generate(publicId);
   }
}
