package com.art.experience.dev.model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DTOBarberReserves {

    private LocalDate findReserveByThisDate;
    private Long BarberId;

    DTOBarberReserves() {
    }

    public LocalDate getFindReserveByThisDate() {
        return convertDate(findReserveByThisDate);
    }

    public Long getBarberId() {
        return BarberId;
    }

    private LocalDate convertDate(final LocalDate date) {
        DateTimeFormatter formatDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault());
        String dateString = formatDate.format(date);
        return LocalDate.parse(dateString, formatDate);
    }
}
