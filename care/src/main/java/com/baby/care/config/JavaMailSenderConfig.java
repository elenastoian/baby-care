package com.baby.care.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {

    @Value("${email.server.host}")
    private String emailHost;

    @Value("${email.server.port}")
    private String emailPort;

    @Value("${spring.mail.userName}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Autowired
    public JavaMailSenderConfig(
            @Value("${email.server.host}") String emailHost,
            @Value("${email.server.port}") String emailPort,
            @Value("${spring.mail.userName}") String mailUsername,
            @Value("${spring.mail.password}") String mailPassword

    ) {
        this.emailHost = emailHost;
        this.emailPort = emailPort;
        this.mailUsername = mailUsername;
        this.mailPassword = mailPassword;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Set the mail properties
        mailSender.setHost(this.emailHost);
        mailSender.setPort(Integer.parseInt(this.emailPort));
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);
        mailSender.setDefaultEncoding("UTF-8");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true");

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }
}
