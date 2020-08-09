package com.art.experience.dev.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DTOAvailableTime {

    private LocalDate date;
    private List<LocalTime> hours;

    public DTOAvailableTime() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<LocalTime> getHours() {
        return hours;
    }

    public void setHours(List<LocalTime> hours) {
        this.hours = hours;
    }
}
