package com.art.experience.dev.web;

import com.art.experience.dev.Configuration.RestCrossOriginController;
import com.art.experience.dev.model.Hairdresser;
import com.art.experience.dev.service.HairdresserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestCrossOriginController("/hairdresser")
public class HairdresserRestController {

    private HairdresserService hairdresserService;

    @Autowired
    public HairdresserRestController(final HairdresserService hairdresserService) {
        this.hairdresserService = hairdresserService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Hairdresser> getHairdressers() {
        return hairdresserService.getAllHairdressers();
    }

    @GetMapping("/{id_hairdresser}")
    @ResponseStatus(HttpStatus.OK)
    public Hairdresser getById(@PathVariable("id_hairdresser") final Long hairdresserId) {
        return hairdresserService.findByID(hairdresserId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Hairdresser create(@RequestBody final Hairdresser hair) {
        return hairdresserService.create(hair);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Hairdresser update(@RequestBody final Hairdresser hair) {
        return hairdresserService.update(hair);
    }

    @DeleteMapping("/{id_user}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("id_user") final Long user) {
        hairdresserService.delete(user);
    }

}
