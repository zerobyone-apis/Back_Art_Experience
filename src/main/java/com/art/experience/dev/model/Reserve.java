package com.art.experience.dev.model;

import com.art.experience.dev.Configuration.InstantJsonFormat;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reserves")
@Relation(value = "reserve", collectionRelation = "reserves")
public class Reserve implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "reserve_sequence")
    @SequenceGenerator(name = "reserve_sequence", allocationSize = 1)

    // Reserve identification info
    @Column(name = "reserve_id")
    private Long reserveId;
    @Column(name = "barber_name")
    private String barberName;
    @Column(name = "barb_or_hair_id")
    private Long barberOrHairdresserId;
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "name_client")
    private String nameClient;
    @Column(name = "mail_client")
    private String mailClient;
    @Column(name = "cel_client")
    private Integer celClient;
    @Column(name= "social_number")
    private Long socialNumber;

    // Time reserve day
    @Column(name = "start_time")
    private LocalDateTime startTime;
    // End Time reserve day.
    @Column(name = "end_time")
    private LocalDateTime endTime;
    // Day of Reserve.
    @Column(name = "reserve_date")
    private LocalDate reserveDate;

    // Reserve Description Info
    @Column(name = "work_id")
    private Long workId;
    @Column(name = "type_work")
    private String workToDo;
    @Column(name = "price_work")
    private Double priceWork;
    @Column(name = "work_time")
    private Instant workTime;

    @Column(name = "additional_cost")
    private Double additionalCost;
    @Column(name = "total_cost")
    private Double totalCost;

    // Reserve Analytics info
    @InstantJsonFormat
    @Column(name = "created_on")
    private Instant createOn;
    @Column(name = "created_by")
    private String createBy;
    @Column(name = "updated_by")
    private String updateBy;
    @Column(name = "updated_on")
    private Instant updateOn;
    @Column(name = "status_reserve")
    private Boolean isActive;
    @Column(name = "is_done")
    private Boolean isDone;

    public LocalDate getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(LocalDate reserveDate) {
        this.reserveDate = reserveDate;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Long getReserveId() {
        return reserveId;
    }

    public void setReserveId(Long reserveId) {
        this.reserveId = reserveId;
    }

    public Long getBarberOrHairdresserId() {
        return barberOrHairdresserId;
    }

    public void setBarberOrHairdresserId(Long barberOrHairdresserId) {
        this.barberOrHairdresserId = barberOrHairdresserId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getMailClient() {
        return mailClient;
    }

    public void setMailClient(String mailClient) {
        this.mailClient = mailClient;
    }

    public Integer getCelClient() {
        return celClient;
    }

    public void setCelClient(Integer celClient) {
        this.celClient = celClient;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Instant getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Instant createOn) {
        this.createOn = createOn;
    }

    public Instant getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Instant updateOn) {
        this.updateOn = updateOn;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean success) {
        isActive = success;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public String getWorkToDo() {
        return workToDo;
    }

    public void setWorkToDo(String workToDo) {
        this.workToDo = workToDo;
    }

    public Double getPriceWork() {
        return priceWork;
    }

    public void setPriceWork(Double priceWork) {
        this.priceWork = priceWork;
    }

    public Instant getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Instant workTime) {
        this.workTime = workTime;
    }

    public Double getAdditionalCost() {
        return additionalCost;
    }

    public void setAdditionalCost(Double additionalCost) {
        this.additionalCost = additionalCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
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
        Reserve reserve = (Reserve) o;
        return Objects.equals(reserveId, reserve.reserveId) &&
                barberOrHairdresserId.equals(reserve.barberOrHairdresserId) &&
                clientId.equals(reserve.clientId) &&
                nameClient.equals(reserve.nameClient) &&
                mailClient.equals(reserve.mailClient) &&
                celClient.equals(reserve.celClient) &&
                socialNumber.equals(reserve.socialNumber) &&
                startTime.equals(reserve.startTime) &&
                Objects.equals(endTime, reserve.endTime) &&
                reserveDate.equals(reserve.reserveDate) &&
                Objects.equals(workId, reserve.workId) &&
                workToDo.equals(reserve.workToDo) &&
                priceWork.equals(reserve.priceWork) &&
                workTime.equals(reserve.workTime) &&
                additionalCost.equals(reserve.additionalCost) &&
                Objects.equals(totalCost, reserve.totalCost) &&
                createOn.equals(reserve.createOn) &&
                createBy.equals(reserve.createBy) &&
                Objects.equals(updateBy, reserve.updateBy) &&
                Objects.equals(updateOn, reserve.updateOn) &&
                isActive.equals(reserve.isActive) &&
                Objects.equals(isDone, reserve.isDone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reserveId, barberOrHairdresserId,socialNumber, clientId, nameClient, mailClient, celClient, startTime, endTime, reserveDate, workId, workToDo, priceWork, workTime, additionalCost, totalCost, createOn, createBy, updateBy, updateOn, isActive, isDone);
    }
}

