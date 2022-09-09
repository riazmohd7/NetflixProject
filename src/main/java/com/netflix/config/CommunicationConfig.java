package com.netflix.config;


import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommunicationConfig {

    @Bean
    public Mailer getMailer(){
        String email = System.getenv("fromEmail");
        String password = System.getenv("fromEmailPassword");
        if(email == null || email.length() == 0 || password == null || password.length() == 0){
            System.out.println("either email or password is null");
            System.exit(1);
        }
        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, email, password)
                .buildMailer();
        return mailer;
    }

    @Bean("fromEmail")
    public String getFromEmail(){
        return System.getenv("fromEmail");
    }
    @Bean("fromName")
    public String getFromName(){
        return System.getenv("name");
    }
}
