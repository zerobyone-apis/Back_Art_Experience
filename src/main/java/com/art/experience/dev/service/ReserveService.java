package com.art.experience.dev.service;

import com.art.experience.dev.data.BarberRepository;
import com.art.experience.dev.data.ClientRepository;
import com.art.experience.dev.data.HairdresserRepository;
import com.art.experience.dev.data.ReservesRepository;
import com.art.experience.dev.data.WorkRepository;
import com.art.experience.dev.exception.ClientException;
import com.art.experience.dev.exception.ResourceNotFoundException;
import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.Client;
import com.art.experience.dev.model.DTOBarberReserves;
import com.art.experience.dev.model.Hairdresser;
import com.art.experience.dev.model.Reserve;
import com.art.experience.dev.model.Work;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReserveService {

    private static final Logger LOGGER = LogManager.getLogger(ClientService.class);
    private ReservesRepository reservesRepository;
    private WorkRepository workRepository;
    private BarberRepository barberRepository;
    private HairdresserRepository hairdresserRepository;
    private ClientRepository clientRepository;

    @Autowired
    public ReserveService(final ReservesRepository reservesRepository,
                          final WorkRepository workRepository,
                          final BarberRepository barberRepository,
                          final HairdresserRepository hairdresserRepository,
                          final ClientRepository clientRepository) {
        this.reservesRepository = reservesRepository;
        this.workRepository = workRepository;
        this.barberRepository = barberRepository;
        this.hairdresserRepository = hairdresserRepository;
        this.clientRepository = clientRepository;
    }

    private final Long DURATION_WORK = 1800000l;

    // Busquedas por ID
    public Reserve findByID(final Long reserveId) {
        Optional<Reserve> reserve = reservesRepository.findById(reserveId);
        if (!reserve.isPresent()) {
            LOGGER.error("Reserve ID Not found. " + reserveId);
            throw new ResourceNotFoundException("Reserve ID Not found. " + reserveId);
        }
        return reserve.get();
    }

    public List<Reserve> findReserveByClientID(final Long clientId) {
        List<Reserve> reserves = reservesRepository.findByClientId(clientId).get();
        if (reserves.isEmpty()) {
            LOGGER.error("Reserve ID Not found. " + clientId);
            throw new ResourceNotFoundException("Reserve ID Not found. " + clientId);
        }
        return reserves.stream()
                .map(reserve -> reserve)
                .collect(Collectors.toList());
    }

    public List<Reserve> findReserveByBarberID(final Long berberOrHairId) {
        Optional<List<Reserve>> reserve = reservesRepository.findByBarberOrHairdresserId(berberOrHairId);
        if (!reserve.isPresent()) {
            LOGGER.error("Barber ID Not found. " + berberOrHairId);
            throw new ResourceNotFoundException("Barber ID Not found. " + berberOrHairId);
        }
        return reserve.get();
    }

    // Busqueda generales
    public List<Reserve> getReserves() {
        List<Reserve> clients = reservesRepository.findAll();
        if (clients.isEmpty()) {
            LOGGER.error("Data Not found in the database");
            throw new ResourceNotFoundException("No data of Reserves in the Database.");
        }
        return clients.stream()
                .map(reserve -> reserve)
                .collect(Collectors.toList());
    }
    // Busqueda Reservas activas
    public List<Reserve> getActiveReserves() {
        List<Reserve> reserves = reservesRepository.findAll();
        if (reserves.isEmpty()) {
            LOGGER.error("Data Not found in the database");
            throw new ResourceNotFoundException("No data of Reserves in the Database.");
        }
        return reserves.stream()
                .map(reserve -> reserve)
                .filter(reserve -> reserve.getActive() == true)
                .collect(Collectors.toList());
    }

    // Busqueda Por Fecha en un barbero especifico
    public List<Reserve> getReservesByDateAndBarberId(final DTOBarberReserves infoReserve) {
        Optional<List<Reserve>> reserves = reservesRepository.findByReserveIdAndBarberId(infoReserve.getBarberId(), infoReserve.getFindReserveByThisDate());
        if (reserves.isEmpty()) {
            LOGGER.error("Data Not found in the database");
            throw new ResourceNotFoundException("No data of Reserves in the Database.");
        }
        return reserves.get().stream()
                .map(reserve -> reserve)
                .collect(Collectors.toList());
    }

    private LocalDate convertToLocalDateTime(String date) {
       String alteredDate = date.replace("-","/");
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
       LocalDate newFormatDate = LocalDate.parse(alteredDate,formatter);
       return newFormatDate;
    }

    // Modificacion de reserva
    public Reserve create(final Long clientId, final Reserve reserve) {
        Optional<Client> clientExist = clientRepository.findById(clientId);
        if (clientExist.isEmpty()) {
            LOGGER.error("Client Not Found with ID. REMEMBER ID Barber or Hairdresser should be part of RequestBody");
            throw new ClientException("Client Not Found with ID " + clientId + " REMEMBER ID Barber or Hairdresser should be part of RequestBody");
        }else {
            clientExist.get().setAmountReserves(clientExist.get().getAmountReserves()+1);

            /**TODO:
             * Mi idea es que en interactions se guarde la interaccion del usuario con el sistema Art
             * En este caso especifico la interaccion seria una Reserva.
             * Pero a futuro, tambien se guardaran Likes, Commentarios, Shares(veces compartidas), y más
             * Lo guardo como string para poder hacerle split al string y obtener cada interaccion,
             * A futuro puedo crear un Enum que contenca las interacciones y con el Atributo Name, comparar los resultado
             * Del split y verificar cuantos de cada uno tengo, sin mencionar que ya voy a tener identificado las cantidadades
             * de cada Interaccion,
             *
             * Opcional separarlo en atributos y poder hacer metricas en base a ello.
             *
             * Formato del Spliter ser separado por coma -> ' , '
             *
             * */
            clientExist.get().setInteractions(clientExist.get().getInteractions() +",New Reserve");
            clientRepository.save(clientExist.get());
        }

        // Reserve newReserve object
        Reserve newReserve = new Reserve();

        /************ Reserve Mutable info ************/

        // Identification Info
        newReserve.setBarberOrHairdresserId(reserve.getBarberOrHairdresserId());
        newReserve.setClientId(clientId);

        // Description Info
        newReserve.setNameClient(reserve.getNameClient());
        newReserve.setMailClient(reserve.getMailClient());
        newReserve.setCelClient(reserve.getCelClient());

        //Sets times
        convertInstnatToFormatString(newReserve, reserve);

        /*TODO: CREAR ENUMERADO QUE WORKS CON 1-NAME_WORK 2-TIME_WORK 3-PRICE_WORK*/
        // Work or Service info
        Work work = createWorkByReserve(clientId, reserve);
        newReserve.setWorkId(work.getWorkId());
        newReserve.setWorkToDo(reserve.getWorkToDo());
        newReserve.setWorkTime(Instant.ofEpochMilli(DURATION_WORK));
        newReserve.setPriceWork(reserve.getPriceWork());
        newReserve.setAdditionalCost(reserve.getAdditionalCost());
        newReserve.setTotalCost(sumCost(work.getPriceWork(), reserve.getAdditionalCost() ));
        newReserve.setActive(Boolean.TRUE);

        // Analytics reserve Info
        Barber barber = getBarber(reserve.getBarberOrHairdresserId());
        newReserve.setCreateBy(barber.getName());
        newReserve.setCreateOn(Instant.now());

        return reservesRepository.save(newReserve);
    }

    private void convertInstnatToFormatString(final Reserve newReserve,final Reserve oldReserve) {

        DateTimeFormatter formatDateTime;
        DateTimeFormatter formatDate;

        formatDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        .withZone(ZoneId.systemDefault());
        String startTimeStr = formatDateTime.format(oldReserve.getStartTime());
        LocalDateTime dateUpdated = LocalDateTime.parse(startTimeStr, formatDateTime);

        //TODO: SET START TIME
        newReserve.setStartTime(dateUpdated);

        formatDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault());
        String endTimeStr = formatDateTime.format(oldReserve.getStartTime());
        // Add 30 Minutos for the work of this job.
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatDateTime).plusMinutes(30);

        //TODO: SET END TIME
        newReserve.setEndTime(endTime);

        formatDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withZone(ZoneId.systemDefault());
        LocalDate reserveDate = LocalDateTime.from(oldReserve.getStartTime()).toLocalDate();
        String dateString = formatDate.format(reserveDate);

        //TODO: SET RESERVE DATE
        // Obtengo la fecha para Hacer un endpoint de busqueda de reserva solo por fecha.
        newReserve.setReserveDate(LocalDate.parse(dateString,formatDate));

    }

    public Reserve update(final Reserve updateReserve) {
        Optional<Reserve> reserve = reservesRepository.findById(updateReserve.getReserveId());
        if (!reserve.isPresent()) {
            LOGGER.error("Reserve not Found");
            throw new ResourceNotFoundException("Reserve not Found with this ID " + updateReserve.getReserveId());
        }
        // Reserve newReserve object
        Reserve toUpdateReserve = reserve.get();

        /************ Reserve Mutable info ************/

        /* Immutable reserve Info
         *
         * toUpdateReserve.setClientId(updateReserve.getClientId());
         * toUpdateReserve.setNameClient(updateReserve.getNameClient());
         * toUpdateReserve.setCreateOn(updateReserve.getCreateOn());
         *
         */

        // Mutable reserve Info
        toUpdateReserve.setBarberOrHairdresserId(updateReserve.getBarberOrHairdresserId());
        toUpdateReserve.setCelClient(updateReserve.getCelClient());
        toUpdateReserve.setMailClient(updateReserve.getMailClient());

        // Description Info
        toUpdateReserve.setWorkToDo(updateReserve.getWorkToDo());
        toUpdateReserve.setWorkTime(Instant.ofEpochMilli(DURATION_WORK));

        // Sets times.
        convertInstnatToFormatString(toUpdateReserve,updateReserve);

        // Cost reserve info
        toUpdateReserve.setPriceWork(updateReserve.getPriceWork());
        toUpdateReserve.setAdditionalCost(updateReserve.getAdditionalCost());
        toUpdateReserve.setTotalCost(sumCost(updateReserve.getPriceWork(), updateReserve.getAdditionalCost()));

        Barber barber = getBarber(reserve.get().getBarberOrHairdresserId());
        toUpdateReserve.setUpdateBy(barber.getName());
        toUpdateReserve.setUpdateOn(Instant.now());
        return reservesRepository.save(reserve.get());
    }

    public Reserve cancel(final Long idClient, final Long idReserve) {
        Optional<Client> clientExist = clientRepository.findById(idClient);
        if (!clientExist.isEmpty()) {
            Optional<Reserve> reserve = reservesRepository.findById(idReserve);
            if (!reserve.isPresent()) {
                LOGGER.error("Cancel method: Reserve not Found with this ID:" + idReserve);
                throw new ResourceNotFoundException("Reserve not Found with this ID: " + idReserve);
            }
            Barber barber = getBarber(reserve.get().getBarberOrHairdresserId());
            reserve.get().setActive(Boolean.FALSE);
            reserve.get().setUpdateOn(Instant.now());
            reserve.get().setUpdateBy(barber.getName());

            Work work = workRepository.findById(reserve.get().getWorkId()).get();
            work.setCanceled(true);
            work.setUpdatedOn(Instant.now());
            work.setUpdatedBy(barber.getName());
            workRepository.save(work);
            return reservesRepository.save(reserve.get());
        } else {
            LOGGER.error("Cancel method: Client not found by this ID: " + idClient);
            throw new ResourceNotFoundException("Not user permission found, Client without Permission.");
        }
    }

    public Reserve isDone(final Long idBarber, final Long idReserve) {
        Optional<Barber> barberExist = barberRepository.findById(idBarber);
        if (!barberExist.isEmpty()) {
            Optional<Reserve> reserve = reservesRepository.findById(idReserve);
            if (!reserve.isPresent()) {
                LOGGER.error("IsDone method: Reserve not Found with this ID:" + idReserve);
                throw new ResourceNotFoundException("Reserve not Found with this ID: " + idReserve);
            }
            reserve.get().setActive(Boolean.FALSE);
            reserve.get().setDone(true);
            reserve.get().setUpdateOn(Instant.now());
            reserve.get().setUpdateBy(barberExist.get().getName());

            Work work = workRepository.findById(reserve.get().getWorkId()).get();
            work.setDone(true);
            work.setUpdatedOn(Instant.now());
            work.setUpdatedBy(barberExist.get().getName());
            workRepository.save(work);
            return reservesRepository.save(reserve.get());
        } else {
            LOGGER.error("Cancel method: Barber not found by this ID: " + idBarber);
            throw new ResourceNotFoundException("IsDone method: Barber not found by this ID: " + idBarber);
        }
    }

    public void delete(final Long barberOrHairdresserId, final Long reserveID) {
        Optional<Barber> barberExist = barberRepository.findById(barberOrHairdresserId);
        if (barberExist.isEmpty()) {
            Optional<Hairdresser> hairdresserExist = hairdresserRepository.findById(barberOrHairdresserId);
            if (hairdresserExist.isEmpty()) {
                LOGGER.error("Dalete Method: Barber or Hairdresser not found by this ID " + barberOrHairdresserId);
                throw new ResourceNotFoundException("Barber or HairdresserNotFound not Found by this ID" + barberOrHairdresserId);
            }
        }
        Optional<Reserve> reserveToDelete = reservesRepository.findById(reserveID);
        if (!reserveToDelete.isPresent()) {
            LOGGER.error("Delete Reserve: Reserve not Found by this ID" + reserveID);
            throw new ResourceNotFoundException("Reserve not Found by this ID" + reserveID);
        } else {
            //TODO: En caso de querer borrar la data por el barbero, se le dejara un boton para poder hacerlo
            LOGGER.info("Delete Reserve: Successfully!!");
            reservesRepository.delete(reserveToDelete.get());
        }
    }

    // Utils methods
    private Work createWorkByReserve(final Long barbOrHairID, final Reserve reserve) {
        Work work = new Work();
        Barber barber = getBarber(barbOrHairID);
        if (Objects.isNull(barber)) {
            Hairdresser hairdresser = getHairdresser(barbOrHairID);
            if (Objects.isNull(hairdresser)) {
                LOGGER.error("Hairdresser And Barber not found by this ID: " + barbOrHairID);
                throw new ResourceNotFoundException("Hairdresser And Barber not found by this ID: " + barbOrHairID);
            } else {
                work.setCreatedBy(hairdresser.getUsername());
                work.setBarberOrHairdresserId(barbOrHairID);
                work.setWorkToDo(reserve.getWorkToDo());
                work.setWorkTime(Instant.ofEpochMilli(DURATION_WORK));
                work.setPriceWork(reserve.getPriceWork());
                work.setCanceled(false);
                work.setDone(false);
                work.setCreateOn(Instant.now());
                return workRepository.save(work);
            }
        }
        work.setCreatedBy(barber.getUsername());
        work.setBarberOrHairdresserId(barbOrHairID);
        work.setWorkToDo(reserve.getWorkToDo());
        work.setWorkTime(Instant.ofEpochMilli(DURATION_WORK));
        work.setPriceWork(reserve.getPriceWork());
        work.setCanceled(false);
        work.setDone(false);
        work.setCreateOn(Instant.now());
        return workRepository.save(work);
    }
    private Hairdresser getHairdresser(final Long id) {
        return hairdresserRepository.findById(id).get();
    }
    private Barber getBarber(final Long id) {
        return barberRepository.findById(id).get();
    }
    private Double sumCost(final Double priceWork, final Double additionalCost) {
        Double additionalCostCheck = Objects.isNull(additionalCost) ? 0.0 :additionalCost;
        return (priceWork + additionalCostCheck);
    }
}
