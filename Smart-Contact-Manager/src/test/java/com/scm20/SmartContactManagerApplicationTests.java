package com.scm20;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.scm20.services.EmailService;

@SpringBootTest
class SmartContactManagerApplicationTests {
    
	@Autowired
	private EmailService service;

	@Test
	void contextLoads() {
	}

   @Test
   void sendEmailTest(){
	service.sendEmail("akashshirture0@gmail.com", "just testing purpose only", "email testing");
   }
  
}