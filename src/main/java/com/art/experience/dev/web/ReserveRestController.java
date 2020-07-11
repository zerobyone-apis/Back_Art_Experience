package com.art.experience.dev.web;

import com.art.experience.dev.Configuration.RestCrossOriginController;
import com.art.experience.dev.model.DTOBarberReserves;
import com.art.experience.dev.model.Reserve;
import com.art.experience.dev.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestCrossOriginController("/reserve")
public class ReserveRestController {

    private ReserveService reserveService;

    @Autowired
    public ReserveRestController(final ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Reserve> getReserves() {
        return reserveService.getReserves();
    }

    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public List<Reserve> getActiveReserves() {
        return reserveService.getActiveReserves();
    }

    @GetMapping("/{reserve_id}")
    @ResponseStatus(HttpStatus.OK)
    public Reserve getByReserveId(@PathVariable("reserve_id") final Long reserveId) {
        return reserveService.findByID(reserveId);
    }

    @GetMapping("/client/{client_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Reserve> getReservesByClientId(@PathVariable("client_id") final Long clientId) {
        return reserveService.findReserveByClientID(clientId);
    }

    @GetMapping("/barber/{barb_or_hair_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Reserve> getByBarberId(@PathVariable("barb_or_hair_id") final Long berberOrHairId) {
        return reserveService.findReserveByBarberID(berberOrHairId);
    }

    @PatchMapping("/all/from/barber/byDate")
    @ResponseStatus(HttpStatus.OK)
    public List<Reserve> getAllByBarberIdAndDate(@RequestBody final DTOBarberReserves info) {
        return reserveService.getReservesByDateAndBarberId(info);
    }

    @PostMapping("/{id_client}")
    @ResponseStatus(HttpStatus.CREATED)
    public Reserve create(@PathVariable("id_client") final Long clientId,
                          @RequestBody final Reserve reserve) {
        return reserveService.create(clientId,reserve);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public Reserve update(@RequestBody final Reserve reserve) {
        return reserveService.update(reserve);
    }

    @PatchMapping("/barber/{id_barber}/isdone/{id_reserve}")
    @ResponseStatus(HttpStatus.CREATED)
    public Reserve isDone(@PathVariable("id_barber") final Long idBarber,
                          @PathVariable("id_reserve") final Long idReserve) {
        return reserveService.isDone(idBarber,idReserve);
    }

    @PatchMapping("/client/{id_client}/cancel/{id_reserve}")
    @ResponseStatus(HttpStatus.CREATED)
    public Reserve cancel(@PathVariable("id_client") final Long idClient,
                          @PathVariable("id_reserve") final Long idReserve) {
        return reserveService.cancel(idClient,idReserve);
    }

    @DeleteMapping("/delete/barber/{id_barber_or_hair}/reserve/{id_reserve}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id_barber_or_hair") final Long idBarber,
                           @PathVariable("id_reserve") final Long idReserve) {
        reserveService.delete(idBarber,idReserve);
    }

}
