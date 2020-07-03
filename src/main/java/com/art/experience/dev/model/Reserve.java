package com.art.experience.dev.model;

import com.art.experience.dev.Configuration.InstantJsonFormat;
import org.springframework.hateoas.core.Relation;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
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

    // Time reserve day
    @Column(name = "start_time")
    @InstantJsonFormat
    private Instant startTime;
    // End Time reserve day.
    @Column(name = "end_time")
    @InstantJsonFormat
    private Instant endTime;

    // Reserve Description Info
    @Column(name = "work_id")
    private Long work_id;
    @Column(name = "type_work")
    private String workToDo;
    @Column(name = "price_work")
    private Double priceWork;
    @Column(name = "work_time")
    private Integer workTime;

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

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
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

    public Long getWork_id() {
        return work_id;
    }

    public void setWork_id(Long work_id) {
        this.work_id = work_id;
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

    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserve reserve = (Reserve) o;
        return reserveId.equals(reserve.reserveId) &&
                barberOrHairdresserId.equals(reserve.barberOrHairdresserId) &&
                clientId.equals(reserve.clientId) &&
                nameClient.equals(reserve.nameClient) &&
                mailClient.equals(reserve.mailClient) &&
                celClient.equals(reserve.celClient) &&
                startTime.equals(reserve.startTime) &&
                endTime.equals(reserve.endTime) &&
                Objects.equals(work_id, reserve.work_id) &&
                workToDo.equals(reserve.workToDo) &&
                priceWork.equals(reserve.priceWork) &&
                Objects.equals(workTime, reserve.workTime) &&
                Objects.equals(additionalCost, reserve.additionalCost) &&
                Objects.equals(totalCost, reserve.totalCost) &&
                Objects.equals(createOn, reserve.createOn) &&
                Objects.equals(createBy, reserve.createBy) &&
                Objects.equals(updateBy, reserve.updateBy) &&
                Objects.equals(updateOn, reserve.updateOn) &&
                Objects.equals(isActive, reserve.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reserveId, barberOrHairdresserId, clientId, nameClient, mailClient, celClient, startTime, endTime, work_id, workToDo, priceWork, workTime, additionalCost, totalCost, createOn, createBy, updateBy, updateOn, isActive);
    }
}

