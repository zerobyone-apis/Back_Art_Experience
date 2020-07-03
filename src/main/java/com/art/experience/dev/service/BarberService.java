package com.art.experience.dev.service;

import com.art.experience.dev.data.BarberRepository;
import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BarberService {

    private static final Logger LOGGER = LogManager.getLogger(BarberService.class);
    private final BarberRepository barberRepository;
    private final UserRepository userRepository;

    @Autowired
    private BarberService(final BarberRepository barberRepository,
                          final UserRepository userRepository) {
        this.barberRepository = barberRepository;
        this.userRepository = userRepository;
    }

    public Barber findByID(final Long barberId) {
        Optional<Barber> barber = barberRepository.findById(barberId);
         if (!barber.isPresent()) {
            LOGGER.error("Barber ID Not found. " + barberId);
            throw new ResourceNotFoundException("Barber ID Not found. " + barberId);
        }
        return barber.get();
    }

    public List<Barber> getAllBarbers() {
        List<Barber> barbers = barberRepository.findAll();
        if (barbers.isEmpty()) {
            LOGGER.error("Data Not found in the database");
            throw new ResourceNotFoundException("No data of Barbers in the Database.");
        }
        return barbers.stream()
                .filter(barber -> barber.getBarberId().equals(barber.getBarberId()))
                .collect(Collectors.toList());
    }

    public Barber create(final Barber barb) {

        // New Barber Obj
        Barber newBarber = new Barber();
        try {
            /************ Barber information ************/

            // Mutable User info
            newBarber.setUsername(barb.getUsername());
            newBarber.setPassword(barb.getPassword());
            newBarber.setName(barb.getName());
            newBarber.setCel(barb.getCel());
            newBarber.setEmail(barb.getEmail());
            newBarber.setStartDate(Instant.now());

            // Mutable barber info
            newBarber.setWorkTime(Objects.isNull(barb.getWorkTime()) ? "10:00 a 19:00" : barb.getWorkTime());
            newBarber.setCutsTimes(Objects.isNull(barb.getCutsTimes()) ? "30 min" : barb.getCutsTimes());
            newBarber.setLocalId(Objects.isNull(barb.getLocalId()) ? 1 : barb.getLocalId());
            newBarber.setLocalName(Objects.isNull(barb.getLocalName()) ? "Art Experience Local 1" : barb.getLocalName());
            newBarber.setActive(true);

            // Mutable Analytics info
            newBarber.setAmountOfCuts(Objects.isNull(barb.getAmountOfCuts()) ? 0L : barb.getAmountOfCuts());
            newBarber.setAmountOfClients(Objects.isNull(barb.getAmountOfClients()) ? 0L : barb.getAmountOfClients());
            newBarber.setAmountDailyReserves(Objects.isNull(barb.getAmountDailyReserves()) ? 0L : barb.getAmountDailyReserves());
            newBarber.setAmountOfShares(Objects.isNull(barb.getAmountOfShares()) ? 0L : barb.getAmountOfShares());
            newBarber.setAmountOfComments(Objects.isNull(barb.getAmountOfComments()) ? 0L : barb.getAmountOfComments());
            newBarber.setAmountOflikesOnComments(Objects.isNull(barb.getAmountOflikesOnComments()) ? 0L : barb.getAmountOflikesOnComments());
            newBarber.setPrestige(Objects.isNull(barb.getAmountOflikesOnComments()) ? 4.0 : barb.getPrestige());

            // User Information
            User user = createUser(barb);
            newBarber.setUserId(user.getUserId());

            return barberRepository.save(newBarber);
        } catch (Exception e) {
            LOGGER.error("Something failed on the creation of Barber. " + e.getMessage());
            throw new IllegalArgumentException("Something failed on the creation of Barber. " + e.getMessage());
        }
    }

    public Barber update(final Barber barb){
        Optional<Barber> barber = barberRepository.findById(barb.getBarberId());
        if(!barber.isPresent()){
            LOGGER.error("Barber not found with the ID " + barb.getBarberId());
            throw new ResourceNotFoundException("Barber not found with the ID " + barb.getBarberId());
        }

        /******************* Barber information ****************/

        // My new Barber Update Obj
        Barber updateBarb = barber.get();

        /* Immutable User info
        *
        * updateBarb.setBarberId(updateBarb.getBarberId());
        * updateBarb.setUserId(updateBarb.getUserId());
        * updateBarb.setStartDate(updateBarb.getStartDate());
        */

        // Mutable User info
        updateBarb.setUsername(barb.getUsername());
        updateBarb.setPassword(barb.getPassword());
        updateBarb.setName(barb.getName());
        updateBarb.setCel(barb.getCel());
        updateBarb.setEmail(barb.getEmail());

        // Mutable barber info
        updateBarb.setWorkTime(barb.getWorkTime());
        updateBarb.setCutsTimes(barb.getCutsTimes());
        updateBarb.setLocalId(Objects.isNull(barb.getLocalId()) ? 1 : barb.getLocalId());
        updateBarb.setLocalName(Objects.isNull(barb.getLocalName()) ? "Art Experience Local 1" : barb.getLocalName());

        // Mutable Analytics info
        updateBarb.setAmountOfCuts(barb.getAmountOfCuts());
        updateBarb.setAmountOfClients(barb.getAmountOfClients());
        updateBarb.setAmountDailyReserves(barb.getAmountDailyReserves());
        updateBarb.setAmountOfShares(barb.getAmountOfShares());
        updateBarb.setAmountOfComments(barb.getAmountOfComments());
        updateBarb.setAmountOflikesOnComments(barb.getAmountOflikesOnComments());
        updateBarb.setPrestige(barb.getPrestige());

        if(Objects.nonNull(barb.getEndDate())){
            updateBarb.setEndDate(Instant.now());
            updateBarb.setActive(false);
        }
        updateUser(updateBarb);
        return barberRepository.save(updateBarb);
    }

    public void delete(final Long barberID){
        Optional<Barber> barber = barberRepository.findById(barberID);
        if (!barber.isPresent()){
            LOGGER.error("Barber not Found by this ID" + barberID);
            throw new ResourceNotFoundException("Barber not Found by this ID" + barberID);
        }else{
            barberRepository.delete(barber.get());
        }
    }

    private User createUser(final Barber barb) {
        User user = new User();
        try {
            /****** User Information ********/

            user.setUsername(barb.getUsername());
            user.setPassword(barb.getPassword());
            user.setCreateOn(Instant.now());
            user.setStatus(true);

            return userRepository.save(user);

        } catch (Exception e) {
            LOGGER.error("Error creating the user. " + e.getMessage());
            throw new IllegalArgumentException("Error creating the user. " + e.getMessage());
        }
    }

    private User updateUser(final Barber updateBarb) {
        Optional<User> userOpt = userRepository.findById(updateBarb.getUserId());
        if(!userOpt.isPresent()) {
            LOGGER.error("User not Found");
            throw new ResourceNotFoundException("User not Found with this ID " + updateBarb.getUserId());
        }
        /****** User Information ********/
        // Update user Obj
        User user = userOpt.get();

        // Mutable User Info
        user.setUsername(updateBarb.getUsername());
        user.setPassword(updateBarb.getPassword());
        user.setCreateOn(updateBarb.getStartDate());

        if(Objects.nonNull(updateBarb.getEndDate())){
            user.setDeleteOn(Instant.now());
            user.setStatus(false);
        }
        return userRepository.save(user);
    }

}
