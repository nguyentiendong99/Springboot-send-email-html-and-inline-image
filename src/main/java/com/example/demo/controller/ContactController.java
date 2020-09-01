package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Controller
public class ContactController {
    @Autowired
    JavaMailSender javaMailSender;
    @GetMapping("/contact")
    public String showContactForm(){
        return "contact_form";
    }
    // Send Mail Basic
//    @PostMapping("/contact")
//    public String submitContactForm(HttpServletRequest request){
//        String fullName = request.getParameter("fullname");
//        String email = request.getParameter("email");
//        String subject = request.getParameter("subject");
//        String content = request.getParameter("content");
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("contact@shopme.com.com");
//        message.setTo("dong19069999@gmail.com");
//        String mailSubject = fullName + "has sent a message";
//        String mailContent = "Send name : " + fullName + "\n";
//        mailContent += "Sender E-Mail : " + email +"\n";
//        mailContent += "Subject : " + subject + "\n";
//        mailContent += "Content : " + content + "\n";
//        message.setSubject(mailSubject);
//        message.setText(mailContent);
//        javaMailSender.send(message);
//        return "message";
//    }



    // Send Mail For Html
//    @PostMapping("/contact")
//    public String submitContactForm(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
//        String fullName = request.getParameter("fullname");
//        String email = request.getParameter("email");
//        String subject = request.getParameter("subject");
//        String content = request.getParameter("content");
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        String mailSubject = fullName + "has sent a message";
//        String mailContent = "<p><b>Send name</b> : " + fullName + "</p>";
//        mailContent += "<p><b>Sender E-Mail </b>: " + email +"</p>";
//        mailContent += "<p><b>Subject </b>: " + subject + "</p>";
//        mailContent += "<p><b>Content </b>: " + content + "</p>";
//
//        helper.setFrom  ("dongptit1999@gmail.com" , "Contact for me");
//        helper.setTo("dong19069999@gmail.com");
//        helper.setSubject(mailSubject);
//        helper.setText(mailContent , true);
//        javaMailSender.send(message);
//        return "message";
//    }



    // send mail for inline image and attachment file
    @PostMapping("/contact")
    public String submitContactForm(HttpServletRequest request,
                                    @RequestParam("attachment") MultipartFile multipartFile) throws MessagingException, UnsupportedEncodingException {
        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message  , true);

        String mailSubject = fullName + " has sent a message";
        String mailContent = "<p><b>Send name</b> : " + fullName + "</p>";
        mailContent += "<p><b>Sender E-Mail </b>: " + email +"</p>";
        mailContent += "<p><b>Subject </b>: " + subject + "</p>";
        mailContent += "<p><b>Content </b>: " + content + "</p>";
        mailContent += "<hr><img style='width: 50px ; height: 50px' src= 'cid:logoImage'/>";
        helper.setFrom  ("dongnguyen@gmail.com" , "Mobile Shop");
        helper.setTo("dong19069999@gmail.com");
        helper.setSubject(mailSubject);
        helper.setText(mailContent , true);
        ClassPathResource resource = new ClassPathResource("/static/image/US.png");
        helper.addInline("logoImage" , resource);
        if (!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            InputStreamSource source = new InputStreamSource() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return multipartFile.getInputStream();
                }
            };
            helper.addAttachment(fileName , source);
        }
        javaMailSender.send(message);
        return "message";
    }

}
