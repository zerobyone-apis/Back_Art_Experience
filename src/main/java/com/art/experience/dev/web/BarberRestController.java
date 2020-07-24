package com.art.experience.dev.web;

import com.art.experience.dev.Configuration.RestCrossOriginController;
import com.art.experience.dev.model.Barber;
import com.art.experience.dev.service.BarberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RestCrossOriginController("/barber")
public class BarberRestController {

    private BarberService barberService;

    @Autowired
    public BarberRestController(final BarberService barberService) {
        this.barberService = barberService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Barber> getBarbers() {
        return barberService.getAllBarbers();
    }

    @GetMapping("/{id_barber}")
    @ResponseStatus(HttpStatus.OK)
    public Barber getById(@PathVariable("id_barber") final Long idBarber) {
        return barberService.findByID(idBarber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Barber create(@RequestBody final Barber barber) {
        return barberService.create(barber);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Barber update(@RequestBody final Barber barber) {
        return barberService.update(barber);
    }

    @DeleteMapping("/{id_barber}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id_barber") final Long barber) {
        barberService.delete(barber);
    }

    @DeleteMapping("/deactivate/{id_barber}")
    @ResponseStatus(HttpStatus.OK)
    public void logicDeleteById(@PathVariable("id_barber") final Long barber) {
        barberService.deactivate(barber);
    }

}
