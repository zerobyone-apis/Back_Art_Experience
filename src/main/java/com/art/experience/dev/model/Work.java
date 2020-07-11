package com.art.experience.dev.model;

import com.art.experience.dev.Configuration.InstantJsonFormat;
import io.swagger.models.auth.In;
import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "works")
@Relation(value = "work", collectionRelation = "works")
public class Work implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "work_sequence")
    @SequenceGenerator(name = "work_sequence", allocationSize = 1)

    // Identification Info
    @Column(name = "work_id")
    private Long workId;
    @Column(name = "barb_or_hair_id")
    private Long barberOrHairdresserId;

    // Work Description
    @Column(name = "type_work")
    private String workToDo;
    @Column(name = "price")
    private Double priceWork;
    @Column(name = "work_time")
    private Instant workTime;

    // Analytics Info
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @Column(name = "create_on")
    @InstantJsonFormat
    private Instant createOn;
    @Column(name = "updated_on")
    @InstantJsonFormat
    private Instant updatedOn;

    @Column(name = "is_done")
    private Boolean isDone;
    @Column(name = "is_canceled")
    private Boolean isCanceled;


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getWorkToDo() {
        return workToDo;
    }

    public void setWorkToDo(String workToDo) {
        this.workToDo = workToDo;
    }

    public Instant getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Instant createOn) {
        this.createOn = createOn;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Long getBarberOrHairdresserId() {
        return barberOrHairdresserId;
    }

    public void setBarberOrHairdresserId(Long barberOrHairdresserId) {
        this.barberOrHairdresserId = barberOrHairdresserId;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public Boolean getCanceled() {
        return isCanceled;
    }

    public void setCanceled(Boolean canceled) {
        isCanceled = canceled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Work work = (Work) o;
        return Objects.equals(workId, work.workId) &&
                barberOrHairdresserId.equals(work.barberOrHairdresserId) &&
                workToDo.equals(work.workToDo) &&
                priceWork.equals(work.priceWork) &&
                workTime.equals(work.workTime) &&
                createdBy.equals(work.createdBy) &&
                Objects.equals(updatedBy, work.updatedBy) &&
                createOn.equals(work.createOn) &&
                Objects.equals(updatedOn, work.updatedOn) &&
                isDone.equals(work.isDone) &&
                isCanceled.equals(work.isCanceled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workId, barberOrHairdresserId, workToDo, priceWork, workTime, createdBy, updatedBy, createOn, updatedOn, isDone, isCanceled);
    }
}

