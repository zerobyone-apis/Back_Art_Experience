package com.art.experience.dev.model;

import org.springframework.hateoas.core.Relation;
import javax.persistence.*;
import java.io.Serializable;
import java.time.*;
import java.util.Objects;

@Entity
@Table(name = "barbers")
@Relation(value = "barber", collectionRelation = "barbers")
public class Barber implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "barber_sequence")
    @SequenceGenerator(name = "barber_sequence", allocationSize = 1)
    
    //User Information
    @Column(name = "barber_id")
    private Long barberId;
    @Column(name= "user_id")
    private Long userId;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "cel")
    private Integer cel;
    @Column(name = "start_date")
    private Instant startDate;
    @Column(name = "end_date")
    private Instant endDate;
    
    // Barber Information
    @Column(name = "name")
    private String name;
    @Column(name= "local_id")
    private Long localId;
    @Column(name= "local_name")
    private String localName;
    @Column(name= "work_time") // Horario del barbero EJ: 10:00 a 18:00
    private String workTime;
    @Column(name= "cuts_times") // Tiempos del los servicios, puede ser fijo o individuales Ej: 30 min
    private String cutsTimes;

    // Analytics information
    @Column(name = "amount_of_cuts")
    private Long amountOfCuts;
    @Column(name = "amount_of_clients")
    private Long amountOfClients;
    @Column(name = "amount_of_comments")
    private Long amountOfComments;
    @Column(name = "amount_of_likes_on_comments")
    private Long amountOflikesOnComments;
    @Column(name = "amount_of_shareds")
    private Long amountOfShares;
    @Column(name = "amount_daily_reserves")
    private Long amountDailyReserves;
    @Column(name = "prestige")
    private Double prestige;
    @Column(name = "is_active") // Borrado logico.
    private Boolean isActive;

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Long getAmountDailyReserves() {
        return amountDailyReserves;
    }

    public void setAmountDailyReserves(Long amountDailyReserves) {
        this.amountDailyReserves = amountDailyReserves;
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

    public Long getBarberId() {
        return barberId;
    }

    public void setBarberId(Long barberId) {
        this.barberId = barberId;
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

    public Long getAmountOfCuts() {
        return amountOfCuts;
    }

    public void setAmountOfCuts(Long amountOfCuts) {
        this.amountOfCuts = amountOfCuts;
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

    public Double getPrestige() {
        return prestige;
    }

    public void setPrestige(Double prestige) {
        this.prestige = prestige;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getCutsTimes() {
        return cutsTimes;
    }

    public void setCutsTimes(String cutsTimes) {
        this.cutsTimes = cutsTimes;
    }

    public Long getAmountOfClients() {
        return amountOfClients;
    }

    public void setAmountOfClients(Long amountOfClients) {
        this.amountOfClients = amountOfClients;
    }

    public Long getAmountOfComments() {
        return amountOfComments;
    }

    public void setAmountOfComments(Long amountOfComments) {
        this.amountOfComments = amountOfComments;
    }

    public Long getAmountOflikesOnComments() {
        return amountOflikesOnComments;
    }

    public void setAmountOflikesOnComments(Long amountOflikesOnComments) {
        this.amountOflikesOnComments = amountOflikesOnComments;
    }

    public Long getAmountOfShares() {
        return amountOfShares;
    }

    public void setAmountOfShares(Long amountOfShares) {
        this.amountOfShares = amountOfShares;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Barber barber = (Barber) o;
        return Objects.equals(userId, barber.userId) &&
                username.equals(barber.username) &&
                password.equals(barber.password) &&
                email.equals(barber.email) &&
                cel.equals(barber.cel) &&
                Objects.equals(startDate, barber.startDate) &&
                name.equals(barber.name) &&
                Objects.equals(localId, barber.localId) &&
                Objects.equals(localName, barber.localName) &&
                Objects.equals(amountOfCuts, barber.amountOfCuts) &&
                Objects.equals(amountOfClients, barber.amountOfClients) &&
                Objects.equals(amountOfComments, barber.amountOfComments) &&
                Objects.equals(amountOflikesOnComments, barber.amountOflikesOnComments) &&
                Objects.equals(amountOfShares, barber.amountOfShares) &&
                Objects.equals(prestige, barber.prestige) &&
                Objects.equals(amountDailyReserves, barber.amountDailyReserves) &&
                isActive.equals(barber.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barberId, userId, username, password, email, cel, startDate, endDate, name, localId, localName, workTime, cutsTimes, amountOfCuts, amountOfClients, amountOfComments, amountOflikesOnComments, amountOfShares, prestige, amountDailyReserves, isActive);
    }
}



