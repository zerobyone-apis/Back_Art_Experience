package com.art.experience.dev.web;

import com.art.experience.dev.Configuration.RestCrossOriginController;
import com.art.experience.dev.model.Reserve;
import com.art.experience.dev.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/client/{client_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Reserve> getReservesByClientId(@PathVariable("client_id") final Long clientId) {
        return reserveService.findByClientID(clientId);
    }

    @GetMapping("/{reserve_id}")
    @ResponseStatus(HttpStatus.OK)
    public Reserve getByReserveId(@PathVariable("reserve_id") final Long reserveId) {
        return reserveService.findByID(reserveId);
    }

    @GetMapping("/barber/{barb_or_hair_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Reserve> getByBarberId(@PathVariable("barb_or_hair_id") final Long berberOrHairId) {
        return reserveService.findByBarberID(berberOrHairId);
    }

    @PostMapping("/create/{id_client}")
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

    @PatchMapping("/client/{id_client}/cancel/{id_reserve}")
    @ResponseStatus(HttpStatus.CREATED)
    public Reserve update(@PathVariable("id_client") final Long idClient,
                          @PathVariable("id_reserve") final Long idReserve) {
        return reserveService.cancel(idClient,idReserve);
    }

    @DeleteMapping("/user/{id_barber_or_hair}/reserve/{id_reserve}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id_barber_or_hair") final Long idUser,
                           @PathVariable("id_reserve") final Long idReserve) {
        reserveService.delete(idUser,idReserve);
    }

}
