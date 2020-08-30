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
@Table(name = "users")
@Relation(value = "user", collectionRelation = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "social_number")
    private Long socialNumber;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;


    @Column(name = "create_on")
    private Instant createOn;
    @Column(name = "delete_on")
    private Instant deleteOn;

    @Column(name = "status")
    private Boolean status;
    @Column(name = "is_barber")
    private Boolean isBarber;
    @Column(name = "is_admin")
    private Boolean isAdmin;

    public Long getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(Long socialNumber) {
        this.socialNumber = socialNumber;
    }

    public Boolean getBarber() {
        return isBarber;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean isBarber() {
        return isBarber;
    }

    public void setBarber(Boolean barber) {
        isBarber = barber;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Instant getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Instant createOn) {
        this.createOn = createOn;
    }

    public Instant getDeleteOn() {
        return deleteOn;
    }

    public void setDeleteOn(Instant deleteOn) {
        this.deleteOn = deleteOn;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return status == user.status &&
                Objects.equals(userId, user.userId) &&
                username.equals(user.username) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                Objects.equals(createOn, user.createOn) &&
                Objects.equals(deleteOn, user.deleteOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, email, createOn, deleteOn, status, isBarber, isAdmin);
    }
}
