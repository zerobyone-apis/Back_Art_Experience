package com.art.experience.dev.service;

import com.art.experience.dev.data.HairdresserRepository;
import com.art.experience.dev.data.PriceRepository;
import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.model.BarberShop;
import com.art.experience.dev.model.Work;
import com.art.experience.dev.model.User;
import com.art.experience.dev.model.Hairdresser;
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
public class HairdresserService {

    private static final Logger LOGGER = LogManager.getLogger(HairdresserService.class);
    private final HairdresserRepository hairdresserRepository;
    private final UserRepository userRepository;
    private final PriceRepository priceRepository;

    @Autowired
    private HairdresserService(final HairdresserRepository hairdresserRepository,
                               final UserRepository userRepository,
                               final PriceRepository priceRepository) {
        this.hairdresserRepository = hairdresserRepository;
        this.userRepository = userRepository;
        this.priceRepository = priceRepository;
    }


    public Hairdresser findByID(final Long hairdresserId) {
        Optional<Hairdresser> hairdresser = hairdresserRepository.findById(hairdresserId);
        if (!hairdresser.isPresent()) {
            LOGGER.error("Hairdresser ID Not found. " + hairdresserId);
            throw new ResourceNotFoundException("Hairdresser ID Not found. " + hairdresserId);
        }
        return hairdresser.get();
    }

    public List<Hairdresser> getAllHairdressers() {
        List<Hairdresser> hairdressers = hairdresserRepository.findAll();
        if (hairdressers.isEmpty()) {
            LOGGER.error("Data Not found in the database");
            throw new ResourceNotFoundException("No data of Hairdressers in the Database.");
        }
        return hairdressers.stream()
                .filter(hairdresser -> (hairdresser).getHairdresserId().equals(hairdresser.getHairdresserId()))
                .collect(Collectors.toList());
    }

    public Hairdresser create(final Hairdresser hair) {
        Hairdresser newHairdresser = new Hairdresser();
        try {
            newHairdresser.setName(hair.getName());
            newHairdresser.setUsername(hair.getUsername());
            newHairdresser.setPassword(hair.getPassword());
            newHairdresser.setEmail(hair.getEmail());
            newHairdresser.setCel(hair.getCel());
            newHairdresser.setAmountCuts(0L);
            newHairdresser.setStartDate(Instant.now());
            newHairdresser.setRateOfHairdresser(hair.getRateOfHairdresser());
            newHairdresser.setClientsHairdresser(hair.getClientsHairdresser());
            newHairdresser.setAmountOfReservesByDay(hair.getAmountOfReservesByDay());
            newHairdresser.setShopName(hair.getShopName());
            newHairdresser.setStatus(true);

            User user = createUser(hair);
            newHairdresser.setUserId(user.getUserId());
            return hairdresserRepository.save(newHairdresser);
        } catch (Exception e) {
            LOGGER.error("Something failed on the creation of Hairdresser. " + e.getMessage());
            throw new IllegalArgumentException("Something failed on the creation of Hairdresser. " + e.getMessage());
        }
    }

    public Hairdresser update(final Hairdresser hair) {
        Optional<Hairdresser> hairdresser = hairdresserRepository.findById(hair.getHairdresserId());
        Hairdresser updateHair = new Hairdresser();
        if (!hairdresser.isPresent()) {
            LOGGER.error("Hairdresser not found with the ID " + hair.getHairdresserId());
            throw new ResourceNotFoundException("Hairdresser not found with the ID " + hair.getHairdresserId());
        }
        updateHair.setUserId(hairdresser.get().getUserId());
        updateHair.setHairdresserId(hairdresser.get().getHairdresserId());
        updateHair.setUsername(hair.getUsername());
        updateHair.setPassword(hair.getPassword());
        updateHair.setName(hair.getName());
        updateHair.setCel(hair.getCel());
        updateHair.setEmail(hair.getEmail());
        updateHair.setRateOfHairdresser(hair.getRateOfHairdresser());
        updateHair.setClientsHairdresser(hair.getClientsHairdresser());
        updateHair.setAmountOfReservesByDay(hair.getAmountOfReservesByDay());
        updateHair.setAmountCuts(hair.getAmountCuts());
        updateHair.setStartDate(hairdresser.get().getStartDate());
        updateHair.setShopName(hair.getShopName());
        if (Objects.nonNull(hair.getEndDate())) {
            updateHair.setEndDate(Instant.now());
        }
        updateUser(updateHair);
        return hairdresserRepository.save(updateHair);
    }

    public void delete(final Long hairdresserID) {
        Optional<Hairdresser> hairdresser = hairdresserRepository.findById(hairdresserID);
        if (!hairdresser.isPresent()) {
            LOGGER.error("Hairdresser not Found by this ID" + hairdresserID);
            throw new ResourceNotFoundException("Hairdresser not Found by this ID" + hairdresserID);
        } else {
            hairdresserRepository.delete(hairdresser.get());
        }
    }

    private User createUser(final Hairdresser hair) {
        User user = new User();
        try {
            user.setUsername(hair.getUsername());
            user.setPassword(hair.getPassword());
            user.setCreateOn(Instant.now());
            user.setStatus(true);
            return userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error("Error creating the user. " + e.getMessage());
            throw new IllegalArgumentException("Error creating the user. " + e.getMessage());
        }
    }
    private void updateUser(final Hairdresser updateHair) {
        Optional<User> user = userRepository.findById(updateHair.getUserId());
        if (!user.isPresent()) {
            LOGGER.error("User not Found");
            throw new ResourceNotFoundException("User not Found with this ID " + updateHair.getUserId());
        }
        user.get().setUsername(updateHair.getUsername());
        user.get().setPassword(updateHair.getPassword());
        user.get().setCreateOn(updateHair.getStartDate());
        if (Objects.nonNull(updateHair.getStatus())) {
            user.get().setStatus(false);
        } else {
            user.get().setStatus(true);
        }
        if (Objects.nonNull(updateHair.getEndDate())) {
            user.get().setDeleteOn(Instant.now());
        }
        userRepository.save(user.get());
    }


    private void insertUserNameOnWorkTable(String username) {
        Work newWork = new Work();
        if (Objects.isNull(username)) {
            LOGGER.error("Hairdresser not has an username please try to insert the correct values: " + username);
            throw new ResourceNotFoundException("Hairdresser not has an username please try to insert the correct values: " + username);
        }
            newWork.setCreatedBy(username);
        priceRepository.save(newWork);
    }
    private void setBarberShop(Hairdresser newHairdresser) {
        BarberShop brSHop1 = createBarberShop(newHairdresser);
        newHairdresser.setShopName(brSHop1.getName());
        newHairdresser.setRateOfHairdresser(brSHop1.getRateOfBarberShop());
//        newHairdresser.setOpenTime(newHairdresser.getOpenTime());

    }
    private BarberShop createBarberShop(final Hairdresser newHairdresser) {
        BarberShop brSHop = new BarberShop();
        brSHop.setAmountOfReservesByDay(newHairdresser.getAmountOfReservesByDay());
        brSHop.setHairdresserId(newHairdresser.getHairdresserId());
        brSHop.setCel(newHairdresser.getCel());
        brSHop.setHairdresserId(newHairdresser.getHairdresserId());
        brSHop.setEmail(newHairdresser.getEmail());
        brSHop.setStartDate(Instant.now());
        brSHop.setName(newHairdresser.getShopName());
        brSHop.setOpenTime(Instant.ofEpochMilli(36000000));//TODO: defoult 10hrs
        brSHop.setRateOfBarberShop("New");
        return brSHop;
    }

}
