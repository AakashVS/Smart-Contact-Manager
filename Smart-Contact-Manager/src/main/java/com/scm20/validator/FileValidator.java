package com.scm20.validator;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile,MultipartFile >{

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 2; 

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    
        if(file==null || file.isEmpty()){
            // context.disableDefaultConstraintViolation();
            // context.buildConstraintViolationWithTemplate("file cannot be empty").addConstraintViolation();
            return true;

        }

        System.out.println("file size:"+file.getSize());
        //file size
        if (file.getSize()> MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("file size should be less than 2MB").addConstraintViolation();
            return false;

        }

        return true;
   

  }
}
