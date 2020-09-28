package com.art.experience.dev.model;

import com.art.experience.dev.Configuration.InstantJsonFormat;
import org.springframework.hateoas.core.Relation;
import javax.persistence.*;
import java.io.Serializable;
import java.time.*;
import java.util.Objects;

@Entity
@Table(name = "clients")
@Relation(value = "client", collectionRelation = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "client_sequence")
    @SequenceGenerator(name = "client_sequence", allocationSize = 1)
    @Column(name = "client_id")
    private Long clientId;
    @Column(name= "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "cel")
    private Integer cel;
    @Column(name = "social_number")
    private Long socialNumber;

    @InstantJsonFormat
    @Column(name = "start_date")
    private Instant startDate;
    @InstantJsonFormat
    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "amount_of_reserves")
    private Long amountReserves;
    @Column(name = "amount_of_interactions")
    private String interactions;
    @Column(name = "status_of_client")
    private Boolean status;
    @Column(name = "type_of_bound_client")
    private String clientType;
    @InstantJsonFormat
    @Column(name = "last_date_updated")
    private Instant lastDateUpdated;

    public boolean isStatus() {
        return status;
    }

    public Instant getLastDateUpdated() {
        return lastDateUpdated;
    }

    public void setLastDateUpdated(Instant lastDateUpdated) {
        this.lastDateUpdated = lastDateUpdated;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCel() {
        return cel;
    }

    public void setCel(Integer cel) {
        this.cel = cel;
    }

    public Long getAmountReserves() {
        return amountReserves;
    }

    public void setAmountReserves(Long amountReserves) {
        this.amountReserves = amountReserves;
    }

    public String getInteractions() {
        return interactions;
    }

    public void setInteractions(String interactions) {
        this.interactions = interactions;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Long getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(Long socialNumber) {
        this.socialNumber = socialNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(clientId, client.clientId) &&
                Objects.equals(userId, client.userId) &&
                name.equals(client.name) &&
                username.equals(client.username) &&
                password.equals(client.password) &&
                email.equals(client.email) &&
                Objects.equals(cel, client.cel) &&
                socialNumber.equals(client.socialNumber) &&
                startDate.equals(client.startDate) &&
                Objects.equals(endDate, client.endDate) &&
                Objects.equals(amountReserves, client.amountReserves) &&
                Objects.equals(interactions, client.interactions) &&
                status.equals(client.status) &&
                Objects.equals(clientType, client.clientType) &&
                Objects.equals(lastDateUpdated, client.lastDateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, userId, name, username, password, email, cel, socialNumber, startDate, endDate, amountReserves, interactions, status, clientType, lastDateUpdated);
    }
}
