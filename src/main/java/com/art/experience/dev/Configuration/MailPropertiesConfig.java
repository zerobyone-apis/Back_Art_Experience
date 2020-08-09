package com.art.experience.dev.Configuration;

import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.security.GeneralSecurityException;
import java.util.Properties;


@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailPropertiesConfig {

    private static final Logger LOGGER = LogManager.getLogger(MailPropertiesConfig.class);

    private String username;
    private String password;
    private String host;
    private String port;
    private String[] to;
    private String auth;
    private String enable;
    private String required;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    private Properties setMailProperties() throws GeneralSecurityException {
        MailSSLSocketFactory sslSocketToManageAllHost = new MailSSLSocketFactory();
        sslSocketToManageAllHost.setTrustAllHosts(true);
        Properties propertiesRequiredByMail = new Properties();
        propertiesRequiredByMail.put("mail.smtp.starttls.enable", this.enable);
        propertiesRequiredByMail.put("mail.smtp.user", this.username);
        propertiesRequiredByMail.put("mail.smtp.password", this.password);
        propertiesRequiredByMail.put("mail.smtp.host", this.host);
        propertiesRequiredByMail.put("mail.smtp.port", this.port);
        propertiesRequiredByMail.put("mail.smtp.auth", this.auth);
        propertiesRequiredByMail.put("mail.smtp.ssl.socketFactory", sslSocketToManageAllHost);
        return propertiesRequiredByMail;
    }

    @Bean
    public Session getSession(){
        try {
          //Session.getDefaultInstance(setMailProperties());
          return Session.getInstance(setMailProperties(), new javax.mail.Authenticator() {
              protected PasswordAuthentication getPasswordAuthentication(){
                  return new PasswordAuthentication(username,password);
              }
          });
        } catch (GeneralSecurityException e) {
           LOGGER.error("ERROR MAKING THE SESSION INSTANCE: " +  e.getMessage());
        }
        return null;
    }

}
