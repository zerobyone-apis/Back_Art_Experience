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
import com.art.experience.dev.model.DTOAvailableTime;
import com.art.experience.dev.model.DTOBarberReserves;
import com.art.experience.dev.model.Hairdresser;
import com.art.experience.dev.model.Reserve;
import com.art.experience.dev.model.Work;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReserveService {

    private static final Logger LOGGER = LogManager.getLogger(ClientService.class);
    private final ReservesRepository reservesRepository;
    private final WorkRepository workRepository;
    private final BarberRepository barberRepository;
    private final HairdresserRepository hairdresserRepository;
    private final ClientRepository clientRepository;
    private final SendMailService sendMailService;

    @Autowired
    public ReserveService(final ReservesRepository reservesRepository,
                          final WorkRepository workRepository,
                          final BarberRepository barberRepository,
                          final HairdresserRepository hairdresserRepository,
                          final ClientRepository clientRepository,
                          final SendMailService sendMailService) {
        this.reservesRepository = reservesRepository;
        this.workRepository = workRepository;
        this.barberRepository = barberRepository;
        this.hairdresserRepository = hairdresserRepository;
        this.clientRepository = clientRepository;
        this.sendMailService = sendMailService;
    }

    // Duration Time 40 minutes
    private final Long DURATION_WORK = 2400000L;
    private final String UPDATE_RESERVE_PARAM = ", Update Reserve";
    private final String CREATE_RESERVE_PARAM = ", New Reserve";

    public Reserve findByID(final Long reserveId) {
        Optional<Reserve> reserve = reservesRepository.findById(reserveId);
        if (reserve.isPresent()) {
            return reserve.get();
        }
        LOGGER.error("Reserve ID Not found. " + reserveId);
        throw new ResourceNotFoundException("Reserve ID Not found. " + reserveId);
    }

    public List<Reserve> findReserveByClientID(final Long clientId) {
        List<Reserve> reserves = reservesRepository.findByClientId(clientId).get();
        if (reserves.isEmpty()) {
            LOGGER.error("Reserve ID Not found. " + clientId);
            throw new ResourceNotFoundException("Reserve ID Not found. " + clientId);
        }
        return reserves.stream()
                .filter(Reserve::getActive)
                .collect(Collectors.toList());
    }

    public List<Reserve> findReserveByBarberID(final Long berberOrHairId) {
        Optional<List<Reserve>> reserve = reservesRepository.findByBarberOrHairdresserId(berberOrHairId);
        if (reserve.isPresent()) {
            return reserve.get();
        }
        LOGGER.error("Barber ID Not found. " + berberOrHairId);
        throw new ResourceNotFoundException("Barber ID Not found. " + berberOrHairId);
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

    public List<Reserve> getActiveReserves() {
        List<Reserve> reserves = reservesRepository.findAll();
        if (reserves.isEmpty()) {
            LOGGER.error("Data Not found in the database");
            throw new ResourceNotFoundException("No data of Reserves in the Database.");
        }
        return reserves.stream()
                .filter(Reserve::getActive)
                .collect(Collectors.toList());
    }

    public List<DTOAvailableTime> getBarberAvailableTimeByDate(final Long barberId) {

        List<DTOAvailableTime> listOfDatesAndTimes = new ArrayList<>();
        LocalDateTime updateDate = LocalDateTime.now();

        LOGGER.info("START PROCESS TO GET AVAILABLE TIME FOR BARBER ID -> " + barberId);
        Barber barberFound = getBarber(barberId);
        if (Objects.isNull(barberFound)) {
            LOGGER.error("SOMETHING WENT WRONG SEARCHING BARBER ID -> " + barberId);
            throw new ResourceNotFoundException("Something went wrong searching barber with ID -> " + barberId);
        }

        LOGGER.info("GETTING RESERVES FOR THIS BARBER");
        List<Reserve> reservesByBarber = findReserveByBarberID(barberId);

        LOGGER.info("FILTERING LIST OF DATETIMES");
        List<LocalDateTime> dateTimes = getFilteredDateTimes(reservesByBarber);


        List<LocalDate> filteredDates = getFilteredDates(dateTimes, updateDate);

        LOGGER.info("GETTING HASHMAP OF DATES , HOURS PER DATE");
        Map<LocalDate, List<LocalTime>> results = getHashMapOfDates(dateTimes, filteredDates);

        LOGGER.info("LISTING DTO OF AVAILABLE DATETIMES");
        results.forEach((date, time) -> {
            // TODO: Convert hashmap to DTO object
            DTOAvailableTime dayAvailable = new DTOAvailableTime();
            dayAvailable.setDate(date);
            dayAvailable.setHours(time);

            // TODO: Add the new DTO to Array of DTOAvailableTimes
            listOfDatesAndTimes.add(dayAvailable);
        });

        return listOfDatesAndTimes;
    }

    private List<LocalDateTime> getFilteredDateTimes(List<Reserve> reservesByBarber) {
        LOGGER.error("FILTERED LIST DATETIMES LIST");
        return reservesByBarber
                .stream()
                .map(Reserve::getStartTime)
                .sorted()
                .collect(Collectors.toList());
    }

    private List<LocalDate> getFilteredDates(List<LocalDateTime> dateTimes, LocalDateTime updateDate) {
        LOGGER.error("START FILTERING DATETIMES TO DATES LIST");
        return dateTimes.stream()
                .sorted()
                .filter(date -> date.isAfter(updateDate))
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .collect(Collectors.toList());
    }

    private HashMap<LocalDate, List<LocalTime>> getHashMapOfDates(List<LocalDateTime> dateTimes, List<LocalDate> filteredDates) {
        LOGGER.info("CREATE HASH MAP WITH DATES AND LIST OF AVAILABLE TIMES");
        HashMap<LocalDate, List<LocalTime>> availableTimePerDay = new HashMap<>();
        dateTimes.stream().sorted().forEach((dateTime) -> {
            for (LocalDate specificDay : filteredDates) {
                //TODO: CREATE HOURS LIST FOR EACH DAY
                List<LocalTime> hours = new ArrayList<>();

                for (LocalDateTime listDateTime : dateTimes) {
                    if (specificDay.equals(listDateTime.toLocalDate())) {
                        hours.add(dateTime.toLocalTime());
                    }
                    //TODO: ADD TO HASHMAP THE KEY VALUE -> UNICS_DAY, LIST<HOURS> FOR THAT DAY
                    availableTimePerDay.put(specificDay, hours);
                }
            }
        });

        return availableTimePerDay;
    }

    public List<Reserve> getReservesByDateAndBarberId(final DTOBarberReserves infoReserve) {
        Optional<List<Reserve>> reserves = reservesRepository.findByReserveIdAndBarberId(infoReserve.getBarberId(), infoReserve.getFindReserveByThisDate());
        if (reserves.isPresent()) {
            return reserves.get().stream()
                    .map(reserve -> reserve)
                    .collect(Collectors.toList());
        }
        LOGGER.error("Data Not found in the database");
        throw new ResourceNotFoundException("No data of Reserves in the Database.");
    }

    public Reserve create(final Long clientId, final Reserve reserve) {

        Client client = clientValidationExists(clientId, CREATE_RESERVE_PARAM);
        Reserve newReserve = new Reserve();

        /************ Reserve Mutable info ************/

        // Identification Info
        newReserve.setBarberOrHairdresserId(reserve.getBarberOrHairdresserId());
        newReserve.setClientId(clientId);

        // Description Info
        newReserve.setNameClient(reserve.getNameClient());
        newReserve.setSocialNumber(client.getSocialNumber());
        newReserve.setMailClient(reserve.getMailClient());
        newReserve.setCelClient(reserve.getCelClient());

        //Sets times
        convertInstantToFormatString(newReserve, reserve);

        /*TODO: MAKE ENUMERATE QUE WORKS CON 1-NAME_WORK 2-TIME_WORK 3-PRICE_WORK*/
        // Work or Service info
        Work work = createWorkByReserve(reserve.getBarberOrHairdresserId(), reserve);
        newReserve.setWorkId(work.getWorkId());
        newReserve.setWorkToDo(reserve.getWorkToDo());
        newReserve.setWorkTime(Instant.ofEpochMilli(DURATION_WORK)); //modify this instant to string or something better
        newReserve.setPriceWork(reserve.getPriceWork());
        newReserve.setAdditionalCost(reserve.getAdditionalCost());
        newReserve.setTotalCost(sumCost(work.getPriceWork(), reserve.getAdditionalCost()));
        newReserve.setActive(Boolean.TRUE);

        // Analytics reserve Info
        Barber barber = getBarber(reserve.getBarberOrHairdresserId());
        newReserve.setCreateBy(barber.getName());
        newReserve.setBarberName(barber.getName());
        newReserve.setCreateOn(Instant.now());

        //Send Notification
        SendReserveNotificationEmail(newReserve);
        return reservesRepository.save(newReserve);

    }

    public Reserve update(final Reserve updateReserve) {

        Client client = clientValidationExists(updateReserve.getClientId(), UPDATE_RESERVE_PARAM);

        Reserve reserve = reservesRepository.findById(updateReserve.getReserveId()).get();
        if (Objects.isNull(reserve)) {
            LOGGER.error("Reserve not Found");
            throw new ResourceNotFoundException("Reserve not Found with this ID " + updateReserve.getReserveId());
        }

        /************ Reserve Mutable info ************/

        // Mutable reserve Info
        reserve.setBarberOrHairdresserId(updateReserve.getBarberOrHairdresserId());
        reserve.setCelClient(updateReserve.getCelClient());
        reserve.setSocialNumber(client.getSocialNumber());
        reserve.setMailClient(updateReserve.getMailClient());

        // Description Info
        reserve.setWorkToDo(updateReserve.getWorkToDo());
        reserve.setWorkTime(Instant.ofEpochMilli(DURATION_WORK));

        // Sets times.
        convertInstantToFormatString(reserve, updateReserve);

        // Cost reserve info
        reserve.setPriceWork(updateReserve.getPriceWork());
        reserve.setAdditionalCost(updateReserve.getAdditionalCost());
        reserve.setTotalCost(sumCost(updateReserve.getPriceWork(), updateReserve.getAdditionalCost()));

        // Set responsible barber
        Barber barber = getBarber(reserve.getBarberOrHairdresserId());
        reserve.setBarberName(barber.getName());
        reserve.setUpdateBy(barber.getName());
        reserve.setUpdateOn(Instant.now());

        //Send Notification
        //TODO: Add Message from update reserve to differentiate of create met_od.
        SendReserveNotificationEmail(reserve);
        return reservesRepository.save(reserve);
    }

    public Reserve cancel(final Long idClient, final Long idReserve) {
        Optional<Client> clientExist = clientRepository.findById(idClient);
        if (clientExist.isPresent()) {
            Optional<Reserve> reserve = reservesRepository.findById(idReserve);
            if (reserve.isPresent()) {
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
                LOGGER.error("Cancel method: Reserve not Found with this ID:" + idReserve);
                throw new ResourceNotFoundException("Reserve not Found with this ID: " + idReserve);
            }
        } else {
            LOGGER.error("Cancel method: Client not found by this ID: " + idClient);
            throw new ResourceNotFoundException("Not user permission found, Client without Permission.");
        }
    }

    public Reserve isDone(final Long idBarber, final Long idReserve) {
        Optional<Barber> barberExist = barberRepository.findById(idBarber);
        if (barberExist.isPresent()) {
            Optional<Reserve> reserve = reservesRepository.findById(idReserve);
            if (reserve.isPresent()) {
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
                LOGGER.error("IsDone method: Reserve not Found with this ID:" + idReserve);
                throw new ResourceNotFoundException("Reserve not Found with this ID: " + idReserve);
            }
        } else {
            LOGGER.error("Cancel method: Barber not found by this ID: " + idBarber);
            throw new ResourceNotFoundException("IsDone method: Barber not found by this ID: " + idBarber);
        }
    }

    public void delete(final Long barberOrHairdresserId, final Long reserveID) {
        Barber barberExist = barberRepository.findById(barberOrHairdresserId).get();
        if (Objects.isNull(barberExist)) {
            Hairdresser hairdresserExist = hairdresserRepository.findById(barberOrHairdresserId).get();
            if (Objects.isNull(hairdresserExist.getStatus())) {
                LOGGER.error("Dalete Method: Barber or Hairdresser not found by this ID " + barberOrHairdresserId);
                throw new ResourceNotFoundException("Barber or HairdresserNotFound not Found by this ID" + barberOrHairdresserId);
            }
        }
        Optional<Reserve> reserveToDelete = reservesRepository.findById(reserveID);
        if (reserveToDelete.isPresent()) {
            //TODO: En caso de querer borrar la data por el barbero, se le dejara un boton para poder hacerlo
            LOGGER.info("Delete Reserve: Successfully!!");
            reservesRepository.delete(reserveToDelete.get());
        } else {
            LOGGER.error("Delete Reserve: Reserve not Found by this ID" + reserveID);
            throw new ResourceNotFoundException("Reserve not Found by this ID" + reserveID);
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

    //TODO: 7o.o)7
    private Hairdresser getHairdresser(final Long id) {
        return hairdresserRepository.findById(id).get();
    }

    private Barber getBarber(final Long id) {
        try {
            LOGGER.info("Finding barber by ID -> " + id);
            return barberRepository.findById(id).get();
        } catch (Exception ex) {
            LOGGER.error("Something went wrong searching barber with id -> " + id);
            throw new ResourceNotFoundException("Something went wrong searching barber with ID -> " + id + "Message: " + ex.getMessage());
        }
    }

    private Double sumCost(final Double priceWork, final Double additionalCost) {
        Double additionalCostCheck = Objects.isNull(additionalCost) ? 0.0 : additionalCost;
        return (priceWork + additionalCostCheck);
    }

    private void convertInstantToFormatString(final Reserve newReserve, final Reserve oldReserve) {

        DateTimeFormatter formatDateTime;
        DateTimeFormatter formatDate;

        formatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        String startTimeStr = formatDateTime.format(oldReserve.getStartTime());
        LocalDateTime dateUpdated = LocalDateTime.parse(startTimeStr, formatDateTime);

        //TODO: SET START TIME
        newReserve.setStartTime(dateUpdated);

        formatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss") // UTC-08:00
                .withZone(ZoneId.systemDefault());
        String endTimeStr = formatDateTime.format(oldReserve.getStartTime());
        // Add 30 Minutos for the work of this job.
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatDateTime).plusMinutes(30);

        //TODO: SET END TIME
        newReserve.setEndTime(endTime);

        formatDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withZone(ZoneId.systemDefault());
        LocalDate reserveDate = LocalDateTime.from(oldReserve.getStartTime()).toLocalDate();
        String dateString = formatDate.format(reserveDate);

        //TODO: SET RESERVE DATE
        // Obtengo la fecha para Hacer un endpoint de busqueda de reserva solo por fecha.
        newReserve.setReserveDate(LocalDate.parse(dateString, formatDate));

    }

    private Client clientValidationExists(final Long clientId, final String actionClient) {
        Optional<Client> clientExist = clientRepository.findById(clientId);
        if (clientExist.isPresent()) {
            clientExist.get().setAmountReserves(clientExist.get().getAmountReserves() + 1);

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
            //TODO: In future add some logic to count how many of one types actions have, to manage better the string caracter lenght.
            //      What we need is clasificate if have 100 = new reserve -> [ Vitaly Client Level ] this person is a great client, etc.
            clientExist.get().setInteractions(clientExist.get().getInteractions() + actionClient);
            return clientRepository.save(clientExist.get());
        } else {
            LOGGER.error("Client Not Found with ID. REMEMBER ID Barber or Hairdresser should be part of RequestBody");
            throw new ClientException("Client Not Found with ID " + clientId + " REMEMBER ID Barber or Hairdresser should be part of RequestBody");
        }
    }

    private LocalDate convertToLocalDateTime(String date) {
        String alteredDate = date.replace("-", "/");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(alteredDate, formatter);
    }

    // Notification methods
    private void SendReserveNotificationEmail(final Reserve reserveDetails) {
        final StringBuilder reserveFormat = new StringBuilder();
        final StringBuilder dateReserve = new StringBuilder();
        Barber barber = getBarber(reserveDetails.getBarberOrHairdresserId());

        final String nameBarber = barber.getName();
        final String instagramBarber = barber.getInstagram();
        final String facebookBarber = barber.getFacebook();
        final String date = LocalDate.from(reserveDetails.getStartTime()).toString();
        final String time = LocalTime.from(reserveDetails.getStartTime()).toString();
        reserveFormat
                .append("<li type=\"square\"> Reserva creada por: ").append(reserveDetails.getNameClient()).append("</li>")
                .append("<li type=\"square\"> Tu Reserva con ").append(nameBarber).append(" fue agendada con exito!").append("</li>")
                .append("<li type=\"square\"> L@ esperamos en Dr César Piovene 1027, Pando, Departamento de Canelones, Uruguay.").append("</li>")
                .append("<li type=\"square\"> Puedes seguir a ").append(nameBarber).append(" en sus redes sociales y estar actualizado con las ultimas tendencias!")
                .append("<li type=\"square\"> Instagram: ").append(Objects.nonNull(instagramBarber) ? instagramBarber : "https://www.instagram.com/artexperiencee/").append("</li>")
                .append("<li type=\"square\"> Facebook: ").append(Objects.nonNull(facebookBarber) ? facebookBarber : "https://www.facebook.com/artexperiencee/").append("</li>");

        dateReserve
                .append("<li type=\"square\"> Fecha: ").append(date).append("</li>")
                .append("<li type=\"square\"> Hora: ").append(time).append("</li>");

        sendMailService.notifyAndSendEmail(reserveFormat.toString(), reserveDetails.getNameClient(), dateReserve.toString(), reserveDetails.getMailClient());
    }

    // Test Methods
    public void testMail() {
        sendMailService.notifyAndSendEmail("<li type=\"square\">Esto es un Test del email</li>", "Juan Miguel", "<li type=\"square\">Fecha: 21-08-2020</li> <li type=\"square\">Hora: 12:00 hrs</li>", "");
    }
}
