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
import com.art.experience.dev.model.Hairdresser;
import com.art.experience.dev.model.Reserve;
import com.art.experience.dev.model.Work;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    public Reserve findByID(final Long reserveId) {
        Optional<Reserve> reserve = reservesRepository.findById(reserveId);
        if (!reserve.isPresent()) {
            LOGGER.error("Reserve ID Not found. " + reserveId);
            throw new ResourceNotFoundException("Reserve ID Not found. " + reserveId);
        }
        return reserve.get();
    }

    public List<Reserve> findByClientID(final Long clientId) {
        List<Reserve> reserves = reservesRepository.findByClientId(clientId).get();
        if (reserves.isEmpty()) {
            LOGGER.error("Reserve ID Not found. " + clientId);
            throw new ResourceNotFoundException("Reserve ID Not found. " + clientId);
        }
        return reserves.stream()
                .map(reserve -> reserve)
                .collect(Collectors.toList());
    }

    public List<Reserve> findByBarberID(final Long berberOrHairId) {
        Optional<List<Reserve>> reserve = reservesRepository.findByBarberOrHairdresserId(berberOrHairId);
        if (!reserve.isPresent()) {
            LOGGER.error("Barber ID Not found. " + berberOrHairId);
            throw new ResourceNotFoundException("Barber ID Not found. " + berberOrHairId);
        }
        return reserve.get();
    }

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

    public Reserve create(final Long clientId, final Reserve reserve) {
        Optional<Client> clientExist = clientRepository.findById(clientId);
        if (clientExist.isEmpty()) {
            LOGGER.error("Client Not Found with ID. REMEMBER ID Barber or Hairdresser should be part of RequestBody");
            throw new ClientException("Client Not Found with ID " + clientId + " REMEMBER ID Barber or Hairdresser should be part of RequestBody");
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
        newReserve.setStartTime(reserve.getStartTime());
        newReserve.setEndTime(reserve.getEndTime());

        // Work or Service info
        Work work = createWorkByReserve(clientId, reserve);
        newReserve.setWork_id(work.getWorkId());
        newReserve.setWorkToDo(reserve.getWorkToDo());
        newReserve.setWorkTime(reserve.getWorkTime());
        newReserve.setPriceWork(reserve.getPriceWork());
        newReserve.setAdditionalCost(reserve.getAdditionalCost());
        newReserve.setTotalCost(sumCost(work.getPriceWork(), reserve.getAdditionalCost()));
        newReserve.setActive(Boolean.TRUE);

        // Analytics reserve Info
        Barber barber = getBarber(reserve.getBarberOrHairdresserId()).get();
        newReserve.setCreateBy(barber.getName());
        newReserve.setCreateOn(Instant.now());

        return reservesRepository.save(newReserve);
    }

    public void delete(final Long barberOrHairdresserId,
                       final Long reserveID) {
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
        */

        // Mutable reserve Info
        toUpdateReserve.setBarberOrHairdresserId(updateReserve.getBarberOrHairdresserId());
        toUpdateReserve.setCelClient(updateReserve.getCelClient());
        toUpdateReserve.setMailClient(updateReserve.getMailClient());

        // Description Info
        toUpdateReserve.setWorkToDo(updateReserve.getWorkToDo());
        toUpdateReserve.setWorkTime(updateReserve.getWorkTime());
        toUpdateReserve.setStartTime(updateReserve.getStartTime());
        toUpdateReserve.setEndTime(updateReserve.getEndTime());
        toUpdateReserve.setUpdateOn(Instant.now());

        // Cost reserve info
        toUpdateReserve.setPriceWork(updateReserve.getPriceWork());
        toUpdateReserve.setAdditionalCost(updateReserve.getAdditionalCost());
        toUpdateReserve.setTotalCost(sumCost(updateReserve.getPriceWork(), updateReserve.getAdditionalCost()));

        Barber barber = getBarber(reserve.get().getBarberOrHairdresserId()).get();
        toUpdateReserve.setUpdateBy(barber.getName());

        return reservesRepository.save(reserve.get());
    }

    public Reserve cancel(final Long idClient,
                          final Long idReserve) {
        Optional<Client> clientExist = clientRepository.findById(idClient);
        if (!clientExist.isEmpty()) {
            Optional<Reserve> reserve = reservesRepository.findById(idReserve);
            if (!reserve.isPresent()) {
                LOGGER.error("Cancel method: Reserve not Found with this ID:" + idReserve);
                throw new ResourceNotFoundException("Reserve not Found with this ID: " + idReserve);
            }
            reserve.get().setActive(Boolean.FALSE);
            reserve.get().setUpdateOn(Instant.now());
            return reservesRepository.save(reserve.get());
        } else {
            LOGGER.error("Cancel method: Client not found by this ID: " + idClient);
            throw new ResourceNotFoundException("Not user permission found, Client without Permission.");
        }
    }

    private Work createWorkByReserve(final Long barbOrHairID, final Reserve reserve) {
        Work work = new Work();
        Barber barber = getBarber(barbOrHairID).get();
        if (Objects.isNull(barber)) {
            Hairdresser hairdresser = getHairdresser(barbOrHairID).get();
            if (Objects.isNull(hairdresser)) {
                LOGGER.error("Hairdresser And Barber not found by this ID: " + barbOrHairID);
                throw new ResourceNotFoundException("Hairdresser And Barber not found by this ID: " + barbOrHairID);
            } else {
                work.setCreatedBy(hairdresser.getUsername());
                work.setBarberOrHairdresserId(barbOrHairID);
                work.setWorkToDo(reserve.getWorkToDo());
                work.setWorkTime(reserve.getWorkTime());
                work.setPriceWork(reserve.getPriceWork());
                work.setCreateOn(Instant.now());
                return workRepository.save(work);
            }
        }
        work.setCreatedBy(barber.getUsername());
        work.setBarberOrHairdresserId(barbOrHairID);
        work.setWorkToDo(reserve.getWorkToDo());
        work.setWorkTime(reserve.getWorkTime());
        work.setPriceWork(reserve.getPriceWork());
        work.setCreateOn(Instant.now());
        return workRepository.save(work);
    }

    private Optional<Hairdresser> getHairdresser(final Long id) {
        return hairdresserRepository.findById(id);
    }

    private Optional<Barber> getBarber(final Long id) {
        return barberRepository.findById(id);
    }

    private Double sumCost(final Double priceWork, final Double additionalCost) {
        return (priceWork + additionalCost);
    }
}
