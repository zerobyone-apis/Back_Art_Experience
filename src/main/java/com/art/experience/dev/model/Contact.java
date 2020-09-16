package com.art.experience.dev.model;

import org.springframework.hateoas.core.Relation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "contact")
@Relation(value = "contact", collectionRelation = "contacts")
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_sequence")
    @SequenceGenerator(name = "contact_sequence", allocationSize = 1)

    //if User exist -> Information
    @Column(name = "id")
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "username")
    private String username;

    // Message Contact Information
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "cel_phone")
    private String celPhone;
    @Column(name = "subject")
    private String subject;
    @Column(name = "description")
    private String description;
    @Column(name = "email_from")
    private String emailFrom;
    @Column(name = "email_to")
    private String emailTo;

    // Times information
    @Column(name = "created_on")
    private Instant createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCelPhone() {
        return celPhone;
    }

    public void setCelPhone(String celPhone) {
        this.celPhone = celPhone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id) &&
                Objects.equals(userId, contact.userId) &&
                Objects.equals(username, contact.username) &&
                fullName.equals(contact.fullName) &&
                celPhone.equals(contact.celPhone) &&
                subject.equals(contact.subject) &&
                description.equals(contact.description) &&
                emailFrom.equals(contact.emailFrom) &&
                emailTo.equals(contact.emailTo) &&
                Objects.equals(createdOn, contact.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, username, fullName, celPhone, subject, description, emailFrom, emailTo, createdOn);
    }
}



