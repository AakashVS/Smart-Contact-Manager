package com.scm20.services;

public interface EmailService {

    //
    void sendEmail(String to,String subject,String body);

    //
    void sendEmailWithHtml();

    //
    void sendEmailWithAttachment();
    
}
