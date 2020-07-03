package com.art.experience.dev.model;

import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "hairdressers")
@Relation(value = "hairdresser", collectionRelation = "hairdressers")
public class Hairdresser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "hairdresser_sequence")
    @SequenceGenerator(name = "hairdresser_sequence", allocationSize = 1)
    @Column(name = "hairdresser_id")
    private Long hairdresserId;
    @Column(name= "user_id")
    private Long userId;
    @Column(name = "shop_id")
    private Long shopId; //TODO: logica de negocio, cuando me llegue el nombre de la barberia , buscarlo y grabar el id de la barberia.
    @Column(name = "name")
    private String name;
    @Column(name = "shop_name")
    private String shopName;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "cel")
    private Integer cel;
    @Column(name = "amount_of_cuts")
    private Long amountCuts;
    @Column(name = "amount_of_clients")
    private Long clientsHairdresser;
    @Column(name = "start_date")
    private Instant startDate;
    @Column(name = "end_date")
    private Instant endDate;
    @Column(name = "rate_of_hairdresser")
    private String rateOfHairdresser;
    @Column(name = "amount_of_reserves_day")
    private String amountOfReservesByDay;
    @Column(name = "status")
    private Boolean status;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getAmountOfReservesByDay() {
        return amountOfReservesByDay;
    }

    public void setAmountOfReservesByDay(String amountOfReservesByDay) {
        this.amountOfReservesByDay = amountOfReservesByDay;
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

    public Long getHairdresserId() {
        return hairdresserId;
    }

    public void setHairdresserId(Long hairdresserId) {
        this.hairdresserId = hairdresserId;
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

    public Long getAmountCuts() {
        return amountCuts;
    }

    public void setAmountCuts(Long amountCuts) {
        this.amountCuts = amountCuts;
    }

    public Long getClientsHairdresser() {
        return clientsHairdresser;
    }

    public void setClientsHairdresser(Long clientsHairdresser) {
        this.clientsHairdresser = clientsHairdresser;
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

    public String getRateOfHairdresser() {
        return rateOfHairdresser;
    }

    public void setRateOfHairdresser(String rateOfHairdresser) {
        this.rateOfHairdresser = rateOfHairdresser;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hairdresser that = (Hairdresser) o;
        return hairdresserId.equals(that.hairdresserId) &&
                userId.equals(that.userId) &&
                shopId.equals(that.shopId) &&
                name.equals(that.name) &&
                username.equals(that.username) &&
                password.equals(that.password) &&
                shopName.equals(that.shopName) &&
                email.equals(that.email) &&
                cel.equals(that.cel) &&
                Objects.equals(amountCuts, that.amountCuts) &&
                Objects.equals(clientsHairdresser, that.clientsHairdresser) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(rateOfHairdresser, that.rateOfHairdresser) &&
                Objects.equals(amountOfReservesByDay, that.amountOfReservesByDay) &&
                status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hairdresserId, userId, shopId, name, username, password, email, cel, amountCuts, clientsHairdresser, startDate, endDate, rateOfHairdresser, amountOfReservesByDay, status);
    }
}



