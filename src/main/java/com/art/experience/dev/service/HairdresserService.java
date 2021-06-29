package com.art.experience.dev.service;

import com.art.experience.dev.data.BarberShopRepository;
import com.art.experience.dev.data.HairdresserRepository;
import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.data.WorkRepository;
import com.art.experience.dev.exception.CreateResourceException;
import com.art.experience.dev.model.BarberShop;
import com.art.experience.dev.model.Hairdresser;
import com.art.experience.dev.model.User;
import com.art.experience.dev.model.Work;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HairdresserService {

    private static final Logger LOGGER = LogManager.getLogger(HairdresserService.class);
    private final HairdresserRepository hairdresserRepository;
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final BarberShopRepository barberShopRepository;

    @Autowired
    private HairdresserService(final HairdresserRepository hairdresserRepository,
                               final UserRepository userRepository,
                               final BarberShopRepository barberShopRepository,
                               final WorkRepository workRepository) {
        this.barberShopRepository = barberShopRepository;
        this.hairdresserRepository = hairdresserRepository;
        this.userRepository = userRepository;
        this.workRepository = workRepository;
    }

    public Hairdresser findByID(final Long hairdresserId) {
        Hairdresser hairdresser = hairdresserRepository.findById(hairdresserId).get();
        if (Objects.isNull(hairdresser)) {
            LOGGER.error("Hairdresser ID Not found. " + hairdresserId);
            throw new ResourceNotFoundException("Hairdresser ID Not found. " + hairdresserId);
        }
        return hairdresser;
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

    // TODO: Get all works by hairdresser ID
    public List<Work> getAllWorksForHairdresserId(final Long hairdresserId) {
        Optional<Hairdresser> hairdresser = hairdresserRepository.findById(hairdresserId);
        if (hairdresser.isPresent()) {
            List<Work> totalWorks = workRepository.findAllByWorkId(hairdresser.get().getHairdresserId()).get();
            return totalWorks.stream()
                    .map(work -> work)
                    .sorted()
                    .collect(Collectors.toList());
        } else {
            LOGGER.error("Hairdresser ID Not found. " + hairdresserId);
            throw new ResourceNotFoundException("Hairdresser ID Not found. " + hairdresserId);
        }
    }

    // Nueva logica aplicada, se busca un shop en caso de que no exista se crea.
    public Hairdresser create(final Hairdresser hair) {
        Hairdresser newHairdresser = new Hairdresser();
        final BarberShop shop;
        try {
            newHairdresser.setName(hair.getName());
            newHairdresser.setUsername(hair.getUsername());
            newHairdresser.setPassword(hair.getPassword());
            newHairdresser.setEmail(hair.getEmail());
            newHairdresser.setCel(hair.getCel());

            newHairdresser.setStartDate(Instant.now());

            newHairdresser.setAmountCuts(Objects.isNull(hair.getAmountCuts()) ? 0L : hair.getAmountCuts());
            newHairdresser.setRateOfHairdresser(Objects.isNull(hair.getRateOfHairdresser()) ? 0L : hair.getRateOfHairdresser());
            newHairdresser.setAmountOfReservesByDay(Objects.isNull(hair.getAmountOfReservesByDay()) ? 0L : hair.getAmountOfReservesByDay());
            newHairdresser.setClientsHairdresser(Objects.isNull(hair.getClientsHairdresser()) ? 0L : hair.getClientsHairdresser());

            // Barber shop, find or create
            if (Objects.isNull(hair.getShopName())) {
                shop = setBarberShop(hair);
                newHairdresser.setShopName(Objects.isNull(hair.getShopName()) ? shop.getName() : hair.getShopName());
            } else {
                List<BarberShop> barberShops = barberShopRepository.findByName(hair.getName()).get();
                shop = barberShops.stream()
                        .filter(shopName -> shopName.getName() == hair.getShopName())
                        .findFirst()
                        .get();

                newHairdresser.setShopName(shop.getName());
            }

            newHairdresser.setShopId(shop.getShopId());
            newHairdresser.setStatus(true);

            User user = createUser(hair);
            newHairdresser.setUserId(user.getUserId());

            //TODO: Update the barber_shop list si corresponde para este barbero
            Hairdresser newHair = updateListIdHairdresserOnBarberShop(newHairdresser, shop);
            return newHair;
        } catch (Exception e) {
            LOGGER.error("Something failed on the creation of Hairdresser. " + e.getMessage());
            throw new IllegalArgumentException("Something failed on the creation of Hairdresser. " + e.getMessage());
        }
    }

    private Hairdresser updateListIdHairdresserOnBarberShop(Hairdresser newHairdresser, BarberShop shop) {
        Hairdresser newHair = hairdresserRepository.save(newHairdresser);
        HashMap updateListHaird = new HashMap<>();
        try {
            updateListHaird.put(newHair.getUsername(), newHair.getHairdresserId());
            shop.setHairdresserIds(updateListHaird);
            barberShopRepository.save(shop);
            return newHair;
        } catch (Exception e) {
            LOGGER.error("Error updating barbershop list ID " + e.getMessage());
            throw new CreateResourceException("Error updating barbershop list ID " + e.getMessage());
        }

    }

    // FIXME: Hay que hacer cosas por aca similares a las del Create
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
        if (hairdresser.isPresent()) {
            hairdresserRepository.delete(hairdresser.get());
        } else {
            LOGGER.error("Hairdresser not Found by this ID" + hairdresserID);
            throw new ResourceNotFoundException("Hairdresser not Found by this ID" + hairdresserID);
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

    private BarberShop setBarberShop(Hairdresser newHairdresser) {
        BarberShop brSHop1 = createBarberShop(newHairdresser);
        newHairdresser.setShopName(brSHop1.getName());
        newHairdresser.setRateOfHairdresser(Long.valueOf(brSHop1.getRateOfBarberShop()));
        return brSHop1;
    }

    private BarberShop createBarberShop(final Hairdresser newHairdresser) {
        BarberShop brSHop = new BarberShop();
        HashMap<String, Long> newHairdID = new HashMap<>();
        newHairdID.put(newHairdresser.getUsername(), newHairdresser.getHairdresserId());

        brSHop.setCel(newHairdresser.getCel());
        brSHop.setHairdresserIds(newHairdID);
        brSHop.setEmail(newHairdresser.getEmail());
        brSHop.setStartDate(Instant.now());
        brSHop.setName(newHairdresser.getShopName());
        brSHop.setOpenTime(Instant.ofEpochMilli(36000000));//TODO: defoult 10hrs
        brSHop.setRateOfBarberShop("New");
        return brSHop;
    }

}
