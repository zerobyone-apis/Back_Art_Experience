package Reserves;

import com.art.experience.dev.model.DTOAvailableTime;
import com.art.experience.dev.model.Reserve;
import com.art.experience.dev.model.ReservesDateTimeJson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class ReserveServiceTest {

    @Test
    public void findByID() {
    }

    @Test
    public void findReserveByClientID() {
    }

    @Test
    public void findReserveByBarberID() {
    }

    @Test
    public void getReserves() {
    }

    @Test
    public void getActiveReserves() {
    }

    @Test
    public void getBarberAvailableTimeByDate() {

        //FIXME: OUTPUT RESERVES
        List<Reserve> reservesByBarber = getReservesFromJson();
        System.out.println("OUTPUT RESERVES: " + reservesByBarber);

        // Create list of DateTimes Filetered
        List<LocalDateTime> dateTimes = getFilteredDateTimes(reservesByBarber);

        // Result List of Available Dates And TimesList
        List<DTOAvailableTime> listOfDatesAndTimes = new ArrayList<>();

        // Current date to compare the reserve just be next of this day
        LocalDateTime updateDate = LocalDateTime.now();
        List<LocalDate> filteredDates = getFilteredDates(dateTimes, updateDate);

        Map<LocalDate, List<LocalTime>> results = getHashMapOfDates(dateTimes, filteredDates);
        System.out.println("Result hashmap: " + listOfDatesAndTimes);

        results.forEach((date, time) -> {
            // TODO: Convert hasmap to DTO object
            DTOAvailableTime dayAvailable = new DTOAvailableTime();
            dayAvailable.setDate(date);
            dayAvailable.setHours(time);

            // TODO: Add the new DTO to Array of DTOAvailableTimes
            listOfDatesAndTimes.add(dayAvailable);
        });
        listOfDatesAndTimes.forEach(res -> System.out.println("Result: "));

    }

    private List<LocalDateTime> getFilteredDateTimes(List<Reserve> reservesByBarber) {
        List<LocalDateTime> dateTimes = reservesByBarber
                .stream()
                .map(Reserve::getStartTime)
                .sorted()
                .collect(Collectors.toList());

        //FIXME: OUTPUT DATETIMES
        System.out.println("OUTPUT DATETIMES: " + dateTimes);
        return dateTimes;
    }

    private List<LocalDate> getFilteredDates(List<LocalDateTime> dateTimes, LocalDateTime updateDate) {
        List<LocalDate> filteredDates = dateTimes.stream()
                .sorted()
                .filter(date -> date.isAfter(updateDate))
                .map(LocalDateTime::toLocalDate)
                .distinct()
                .collect(Collectors.toList());

        //FIXME: OUTPUT FILTERED DATES
        System.out.println("FILTERED DATES: " + filteredDates);
        return filteredDates;
    }

    private HashMap<LocalDate, List<LocalTime>> getHashMapOfDates(List<LocalDateTime> dateTimes, List<LocalDate> filteredDates) {
        HashMap<LocalDate, List<LocalTime>> availableTimePerDay = new HashMap<>();
        dateTimes.stream().sorted().forEach((dateTime) -> {
            for (LocalDate unicDay : filteredDates) {
                //TODO: CREATE HOURS LIST FOR EACH DAY
                List<LocalTime> hours = new ArrayList<>();
                System.out.println("Days of Reserves: \n");
                System.out.println("Unique day: "  + unicDay + " \n ");

                for (LocalDateTime listDateTime : dateTimes) {
                    if (unicDay.equals(listDateTime.toLocalDate())) {

                        //TODO: INDEXED HOUR IN FOR LOOP
                        hours.add(dateTime.toLocalTime());
                        System.out.println("New Reserve: " + dateTime.toLocalDate());
                        System.out.println("Filtered day: " + dateTime.toLocalDate());
                        System.out.println("Filtered hour: " + dateTime.toLocalTime() + "\n");

                    }
                    //TODO: ADD TO HASHMAP THE KEY VALUE -> UNICDAY, LIST<HOURS> FOR THAT DAY
                    availableTimePerDay.put(unicDay, hours);
                }
            }
        });

        return availableTimePerDay;
    }

    @Test
    public void getReservesByDateAndBarberId() {
    }

    @Test
    public void create() {
    }

    @Test
    public void update() {
    }

    @Test
    public void cancel() {
    }

    @Test

    public void isDone() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void testMail() {
    }


    private List<Reserve> getReservesFromJson() {
        String reservesJson = "";
        List<Reserve> reserves = new ArrayList<>();
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            reservesJson = Files.lines(Paths.get(loader.getResource("insertReserves.json").toURI()))
                    .parallel()
                    .collect(Collectors.joining());

            reserves = ReservesDateTimeJson.arrayParse(reservesJson);

            return reserves;
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(ReserveServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reserves;
    }
}