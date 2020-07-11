package com.art.experience.dev.service;

import com.art.experience.dev.data.BarberRepository;
import com.art.experience.dev.data.BarberShopRepository;
import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.exception.CreateResourceException;
import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.BarberShop;
import com.art.experience.dev.model.Hairdresser;
import com.art.experience.dev.model.User;
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
public class BarberService {

    private static final Logger LOGGER = LogManager.getLogger(BarberService.class);
    private final BarberRepository barberRepository;
    private final UserRepository userRepository;
    private final BarberShopRepository barberShopRepository;

    @Autowired
    private BarberService(final BarberRepository barberRepository,
                          final UserRepository userRepository,
                          final BarberShopRepository barberShopRepository) {
        this.barberRepository = barberRepository;
        this.userRepository = userRepository;
        this.barberShopRepository = barberShopRepository;
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
        BarberShop shop;
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

            // Barber shop, find or create
            if (Objects.isNull(barb.getLocalName())) {
                shop = createBarberShop(barb);
                newBarber.setLocalName(Objects.isNull(barb.getLocalName()) ? shop.getName() : barb.getLocalName());
            } else {
                List<BarberShop> barberShops = barberShopRepository.findByName(barb.getName()).get();
                shop = barberShops.stream()
                        .filter(shopName -> shopName.getName() == barb.getLocalName())
                        .findFirst()
                        .get();

                newBarber.setLocalName(shop.getName());
            }
            newBarber.setLocalId(shop.getShopId());
            newBarber.setLocalName(shop.getName());

            //TODO: separar las horas de entrada y de salida del barbero en dos atributos separados StartWorkTime
            newBarber.setWorkTime(Objects.isNull(barb.getWorkTime()) ? "10:00 a 19:00" : barb.getWorkTime());
            //TODO: cambiar el tipo de dato de string a Instant y poner la hora que usamos 30 min

            //TODO: hacer una clave foranea para que WORK entity se enlace con el barbero
            //      y hacer una traceabilidad de los trabajos realizados.
            newBarber.setCutsTimes(Objects.isNull(barb.getCutsTimes()) ? "30 min" : barb.getCutsTimes());
            newBarber.setActive(true);

            // Mutable Analytics info
            //TODO: Realizar logica para que a medida vayamos obteniendos los datos de cortes,
            // y de interacciones poder realizar el calculo de las metricas a guardar.

            // TODO: Punto importante es traquear la cantidad de reservas diarias para poder hacer una buen
            //       gestion sobre las reservas en el barbero, esto se puede hacer en consultas a la DB,
            //       Y colocar un cron para que cargue las metricas es decir que haga las consulta y luego guarde la info.
            //       Tambien deberiamos de poder enviar mail, con un archivo adjunto que nos de la informacionn diaria de cada
            //       Barbero, asi como los detalles de costos, estimaciones, y calculos, diarios, semanales, y mensuales del mismo.
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

            //TODO: Update the barber_shop list si corresponde para este barbero
            Barber newBarb = updateListIdBabersOnBarberShop(newBarber, shop);
            return newBarb;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new CreateResourceException(e.getMessage());
        }
    }

    private Barber updateListIdBabersOnBarberShop(final Barber newBarberObj,final BarberShop shop) {
        Barber newBarb = barberRepository.save(newBarberObj);
        HashMap updateListBarberId = new HashMap<>();
        try {
            updateListBarberId.put(newBarb.getUsername(), newBarb.getBarberId());
            shop.setBarberIds(updateListBarberId);
            shop.setUrlLogo("https://drive.google.com/file/d/1R0qlHJ_PH5mS14yCj-Yty3TLYMOoiCer/view?usp=sharing");
            shop.setUrl_banner("https://drive.google.com/file/d/1TTorWJTnRzxAXbl8otAjVr3PPbQfJFvH/view?usp=sharing");
            shop.setUrl_folder_images("https://drive.google.com/file/d/16Fax8Uqsbvqiy8Nf7ae5rIVOCKfWm86p/view?usp=sharing");
            barberShopRepository.save(shop);
            return newBarb;
        } catch (Exception e) {
            LOGGER.error("Error updating barbershop list ID " + e.getMessage());
            throw new CreateResourceException("Error updating barbershop list ID " + e.getMessage());
        }

    }

    private BarberShop createBarberShop(final Barber barb) {
        BarberShop brSHop = new BarberShop();
        HashMap<String, Long> newHairdID = new HashMap<>();
        newHairdID.put(barb.getUsername(), barb.getBarberId());

        brSHop.setCel(barb.getCel());
        brSHop.setBarberIds(newHairdID);
        brSHop.setEmail(barb.getEmail());
        brSHop.setDirections("Pando 7a");
        brSHop.setStartDate(Instant.now());
        brSHop.setName(Objects.isNull(barb.getLocalName()) ? "Art Experience" : barb.getLocalName());
        brSHop.setOpenTime(Instant.ofEpochMilli(36000000));//TODO: defoult 10hrs
        brSHop.setRateOfBarberShop("New");
        return brSHop;
    }

    public Barber update(final Barber barb) {
        Optional<Barber> barber = barberRepository.findById(barb.getBarberId());
        if (!barber.isPresent()) {
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
        updateBarb.setLocalName(Objects.isNull(barb.getLocalName()) ? "Art Experience" : barb.getLocalName());

        // Mutable Analytics info
        updateBarb.setAmountOfCuts(barb.getAmountOfCuts());
        updateBarb.setAmountOfClients(barb.getAmountOfClients());
        updateBarb.setAmountDailyReserves(barb.getAmountDailyReserves());
        updateBarb.setAmountOfShares(barb.getAmountOfShares());
        updateBarb.setAmountOfComments(barb.getAmountOfComments());
        updateBarb.setAmountOflikesOnComments(barb.getAmountOflikesOnComments());
        updateBarb.setPrestige(barb.getPrestige());

        if (Objects.nonNull(barb.getEndDate())) {
            updateBarb.setEndDate(Instant.now());
            updateBarb.setActive(false);
        }
        updateUser(updateBarb);
        return barberRepository.save(updateBarb);
    }

    public void delete(final Long barberID) {
        Optional<Barber> barber = barberRepository.findById(barberID);
        if (!barber.isPresent()) {
            LOGGER.error("Barber not Found by this ID" + barberID);
            throw new ResourceNotFoundException("Barber not Found by this ID" + barberID);
        } else {
            barberRepository.delete(barber.get());
        }
    }

    public void deactivate(final Long barberID) {
        Optional<Barber> barber = barberRepository.findById(barberID);
        if (!barber.isPresent()) {
            LOGGER.error("Barber not Found by this ID" + barberID);
            throw new ResourceNotFoundException("Barber not Found by this ID" + barberID);
        } else {
            barber.get().setActive(false);
            barberRepository.save(barber.get());
        }
    }

    private User createUser(final Barber barb) {
        User user = new User();
        try {
            Optional<User> checkUser = userRepository.findByUsername(barb.getUsername());
            if(!checkUser.isEmpty()){
                LOGGER.error(barb.getUsername() + " already exists, please try with another Username.");
                throw new CreateResourceException(barb.getUsername() + " already exists, please try with another Username.");
            }
            /****** User Information ********/

            user.setUsername(barb.getUsername());
            user.setPassword(barb.getPassword());
            user.setCreateOn(Instant.now());
            user.setStatus(true);

            return userRepository.save(user);

        } catch (Exception e) {
            LOGGER.error("Error creating the user. " + e.getMessage());
            throw new CreateResourceException("Error creating the user. " + e.getMessage());
        }
    }

    private User updateUser(final Barber updateBarb) {
        Optional<User> userOpt = userRepository.findById(updateBarb.getUserId());
        if (!userOpt.isPresent()) {
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

        if (Objects.nonNull(updateBarb.getEndDate())) {
            user.setDeleteOn(Instant.now());
            user.setStatus(false);
        }
        return userRepository.save(user);
    }

}
