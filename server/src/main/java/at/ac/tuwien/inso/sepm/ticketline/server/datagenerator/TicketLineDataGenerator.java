package at.ac.tuwien.inso.sepm.ticketline.server.datagenerator;

import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketSeatStatus;
import at.ac.tuwien.inso.sepm.ticketline.rest.enums.TicketStatus;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ReservationNumberService;
import at.ac.tuwien.inso.sepm.ticketline.server.util.FileUtil;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoUnit.DAYS;

@Profile("generateData")
@Component
public class TicketLineDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketLineDataGenerator.class);
    private static final int NUMBER_OF_EVENT_TO_GENERATE = 200;
    private static final int NUMBER_OF_LOCATION_TO_GENERATE = 50;
    private static final int NUMBER_OF_ARTISTS_TO_GENERATE = 50;
    private static final int NUMBER_OF_HALLPLAN_TEMPLATE = 3;
    private static final String PATH = "/datagenerator/hallplan/";
    private static final String HALL_IMAGE = "hallplan%d_%s.jpg";
    private static final String HALL_CSV = "hallplan%d.csv";
    private static final int NUMBER_OF_CUSTOMER_TO_GENERATE = 1000;
    private static final int NUMBER_OF_TICKET_TO_GENERATE = 1500;

    private final TicketRepository ticketRepository;
    private final TicketSeatRepository ticketSeatRepository;
    private final CustomerRepository customerRepository;
    private final PerformanceRepository performanceRepository;
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;
    private final LocationRepository locationRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final InvoiceRepository invoiceRepository;

    private final ReservationNumberService reservationNumberService;
    private final Faker faker;

    private List<Location> locationList = new ArrayList<>();
    private List<Hall> hallList = new ArrayList<>();
    private List<Artist> artistList = new ArrayList<>();
    private List<Customer> customersList = new ArrayList<>();
    private List<Performance> performancesList = new ArrayList<>();

    private List<List<Seat>> hallplans;


    public TicketLineDataGenerator(TicketRepository ticketRepository, TicketSeatRepository ticketSeatRepository, CustomerRepository customerRepository, EventRepository eventRepository, ArtistRepository artistRepository,
                                   HallRepository hallRepository, LocationRepository locationRepository,
                                   PerformanceRepository performanceRepository, ReservationNumberService reservationNumberService,
                                   SeatRepository seatRepository, InvoiceRepository invoiceRepository){
        this.ticketRepository = ticketRepository;
        this.ticketSeatRepository = ticketSeatRepository;
        this.customerRepository = customerRepository;
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
        this.locationRepository = locationRepository;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
        this.performanceRepository = performanceRepository;
        this.invoiceRepository = invoiceRepository;

        this.reservationNumberService = reservationNumberService;
        this.faker = new Faker();
    }

    @PostConstruct
    private void generate(){
        generateLocation();
        generateHalls();
        generateArtists();
        generatePerformance();
        generateCustomers();
        generateTicket();
        freeResources();
    }

    private void generateLocation(){
        if (locationRepository.count() > 0){
            LOGGER.info("location already generated");
            locationList = locationRepository.findAll();
            return;
        }
        LOGGER.info("generating {} location entries");
        for (int i = 0; i < NUMBER_OF_LOCATION_TO_GENERATE; i++) {
            Location location = Location.builder()
                .city(faker.address().cityName())
                .country(faker.address().country())
                .street(faker.address().streetName())
                .houseNumber(Integer.parseInt(faker.address().buildingNumber()))
                .zip(faker.address().zipCode())
                .description(faker.company().name())
                .build();
            //LOGGER.debug("saving location {}", location);
            locationRepository.save(location);
            locationList.add(location);
        }
    }

    private void generateHalls(){
        if (hallRepository.count() > 0){
            LOGGER.info("halls already generated");
            hallList = hallRepository.findAll();
            return;
        }

        List<List<Seat>> hallplans = new ArrayList<>();
        for (int i = 1; i <= NUMBER_OF_HALLPLAN_TEMPLATE; i++) {
            hallplans.add(loadSeats(i));
        }

        this.hallplans = hallplans;

        LOGGER.info("generating {} hall entries", locationList.size());
        for (Location location : locationList) {
            for (int j = 0; j < faker.number().numberBetween(3, 8); j++) {
                int file_no = j%NUMBER_OF_HALLPLAN_TEMPLATE;

                Hall hall = Hall.builder()
                    .description("Hall " + faker.artist().name())
                    .location(location)
                    .type(HallType.SEAT)
                    .build();

                List<Seat> seats = new ArrayList<>();
                hallplans.get(file_no).forEach(s -> seats.add(Seat.builder().seat(s).hall(hall).build()));
                hall.setSeats(seats);
                hall.setCapacity(seats.size());

                hallRepository.save(hall);
                seatRepository.saveAll(seats);
                hallList.add(hall);
                copyBackgroundImages(file_no+1,hall.getId(),"en","de");
            }
        }
    }

    private void generateArtists(){
        if (artistRepository.count() > 0){
            LOGGER.info("artists already generated");
            artistList = artistRepository.findAll();
            return;
        }
        LOGGER.info("generating {} artist entries", NUMBER_OF_ARTISTS_TO_GENERATE);
        for (int i = 0; i < NUMBER_OF_ARTISTS_TO_GENERATE; i++) {
            Artist artist = Artist.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .build();
            //LOGGER.debug("saving artist {}", artist);
            artistList.add(artist);
            artistRepository.save(artist);
        }
    }

    private void generatePerformance(){
        if (performanceRepository.count() > 0){
            LOGGER.info("performances already generated");
            performancesList = performanceRepository.findAll();
            return;
        }

        LOGGER.info("generating {} event entries", NUMBER_OF_EVENT_TO_GENERATE);
        EventCategory[] eventCategories = EventCategory.values();

        for (int i = 0; i < NUMBER_OF_EVENT_TO_GENERATE; i++) {
            int duration;
            LocalDateTime startDate = LocalDateTime.ofInstant(faker.date().future(365,TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
            Event event = Event.builder()
                .title(faker.music().instrument())
                .description(faker.harryPotter().quote())
                .category(eventCategories[faker.number().numberBetween(0,eventCategories.length-1)])
                .duration(duration = faker.number().numberBetween(60,180))
                .startDate(startDate)
                .endDate(startDate.plusDays(faker.number().numberBetween(10,30)))
                .build();
            //LOGGER.debug("saving event {}", event);
            eventRepository.save(event);

            int NUMBER_OF_PERFORMANCE_TO_GENERATE = faker.number().numberBetween(1,10);
            LOGGER.info("generating {} performance entries", NUMBER_OF_PERFORMANCE_TO_GENERATE);

            for (int j = 0; j < NUMBER_OF_PERFORMANCE_TO_GENERATE; j++) {

                LocalDateTime startDateTime;
                if (j == 0){
                    startDateTime = startDate;
                } else if (j == NUMBER_OF_PERFORMANCE_TO_GENERATE - 1){
                    startDateTime = event.getEndDate();
                } else {
                    long end = DAYS.between(startDate, event.getEndDate());
                    startDateTime = startDate.plusDays(faker.number().numberBetween(1, end));
                }

                Hall hall = hallList.get(faker.number().numberBetween(0, hallList.size()-1));

                Performance performance = Performance.builder()
                    .basePrice((double)faker.number().numberBetween(5, 100))
                    .leftCapacity(hall.getCapacity())
                    .event(event)
                    .hall(hall)
                    .startDateTime(startDateTime)
                    .endDateTime(startDateTime.plusMinutes(duration))
                    .build();
                //LOGGER.debug("saving performance {}", performance);
                performancesList.add(performance);
                for (int k = 0; k < faker.number().numberBetween(1,6); k++) {
                    artistList.get(faker.number().numberBetween(0, artistList.size()-1)).getPlays_in().add(performance);
                }
            }
            performanceRepository.saveAll(performancesList);
        }
        artistRepository.saveAll(artistList);
    }

    private void generateCustomers() {
        if (customerRepository.count() > 0) {
            LOGGER.info("customers already generated");
            customersList = customerRepository.findAll();
            return;
        }
        LOGGER.info("generating {} customers entries");
        String anon = "ANONYMOUS";
        Customer anonymous = Customer.builder()
            .firstname(anon)
            .surname(anon)
            .email(String.format("%s.%s@ticketline.com",anon,anon).toLowerCase())
            .address("UNKNOWN")
            .birthday(LocalDate.of(1,1,1))
            .phoneNumber("UNKNOWN")
            .build();

        customerRepository.save(anonymous);

        for (int i = 0; i < NUMBER_OF_CUSTOMER_TO_GENERATE; i++) {
            String name = faker.name().firstName();
            String surname = faker.name().lastName();
            LocalDate birthday = LocalDate.ofInstant(faker.date().birthday(16,99).toInstant(), ZoneId.systemDefault());
            Customer customer = Customer.builder()
                .firstname(name)
                .surname(surname)
                .email(String.format("%s.%s@ticketline.com",name,surname).toLowerCase())
                .address(faker.address().fullAddress())
                .birthday(birthday)
                .phoneNumber(faker.phoneNumber().phoneNumber())
                .build();

            //LOGGER.debug("saving customer {}", customer);
            customersList.add(customer);
        }
        customerRepository.saveAll(customersList);
    }

    private void generateTicket(){
        if (ticketRepository.count() > 0) {
            LOGGER.info("Tickets already generated");
            return;
        }
        LOGGER.info("generating {} Ticket entries");
        List<Ticket> ticketsList = new ArrayList<>();
        List<TicketSeat> ticketSeatList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TICKET_TO_GENERATE; i++) {
            LocalDateTime dateOfIssue = LocalDateTime.ofInstant(faker.date().past(365,TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
            Customer customer = customersList.get(faker.number().numberBetween(0, customersList.size()-1));
            Performance performance = performancesList.get(faker.number().numberBetween(0,performancesList.size()-1));

            TicketSeatStatus ticketSeatStatus = TicketSeatStatus.SOLD;
            TicketStatus ticketStatus = TicketStatus.S;

            if(i%5 == 0 ){
                ticketStatus = TicketStatus.R;
                ticketSeatStatus = TicketSeatStatus.RESERVED;
            }

            Ticket ticket = Ticket.builder()
                .dateOfIssue(dateOfIssue)
                .reservationNumber(reservationNumberService.save(new ReservationNumber()))
                .status(ticketStatus)
                .customer(customer)
                .performance(performance)
                .build();

            List<Seat> seatList = performance.getHall().getSeats();
            int fromIndex = faker.number().numberBetween(0, seatList.size()-1);
            int toIndex = Math.min(fromIndex + faker.number().numberBetween(1,5), seatList.size()-1);
            int selledSead = toIndex-fromIndex;
            performance.setLeftCapacity(performance.getLeftCapacity()-selledSead);
            double price = 0;
            for(Seat seat : seatList.subList(fromIndex,toIndex)){
                price += seat.getMultiplier() * performance.getBasePrice();
                TicketSeat ticketSeat = TicketSeat.builder()
                    .status(ticketSeatStatus)
                    .performance(performance)
                    .seat(seat)
                    .ticket(ticket)
                    .build();
                ticketSeatList.add(ticketSeat);
            }
            price = Math.round(price * 100.0) / 100.0;
            ticket.setPrice(price);
            ticketsList.add(ticket);
        }
        ticketsList = ticketRepository.saveAll(ticketsList);
        ticketSeatRepository.saveAll(ticketSeatList);
        performanceRepository.saveAll(performancesList);

        List<Invoice> invoiceList = new ArrayList<>();

        LOGGER.info("Generating invoice entries");
        for(Ticket t : ticketsList){
            if(t.getStatus().equals(TicketStatus.S)){
                String invoiceNumber;
                Long numberOfInvoices = invoiceRepository.count();

                invoiceNumber = LocalDate.now().getYear() + String.format("%08d", numberOfInvoices+1);

                //Create invoice
                Invoice.InvoiceBuilder invoicebuilder = Invoice.builder()
                    .ticket(t)
                    .dateOfIssue(LocalDateTime.now())
                    .invoiceNumber(invoiceNumber);

                invoiceRepository.save(invoicebuilder.build());

            }
        }

    }

    private void freeResources(){
        locationList = null;
        hallList = null;
        artistList = null;
        customersList = null;
        performancesList = null;
    }

    private List<Seat> loadSeats(int file_no){
        List<Seat> list = new ArrayList<>();
        String path = String.format(PATH+HALL_CSV,file_no);
        path = getClass().getResource(path).getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("loadSeats()", e);
        }

        int line_index=1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                line_index++;
                String[] cols = line.split(";");
                if (cols.length == 8) {
                    list.add(Seat.builder()
                        .sector(Integer.parseInt(cols[0]))
                        .row(Integer.parseInt(cols[1]))
                        .number(Integer.parseInt(cols[2]))
                        .multiplier(Double.parseDouble(cols[3].replace(",", ".")))
                        .xCoordinate(Integer.parseInt(cols[4]))
                        .yCoordinate(Integer.parseInt(cols[5]))
                        .angle(Integer.parseInt(cols[6]))
                        .type(SeatType.valueOf(cols[7].trim()))
                        .build());
                }
            }
        } catch (Exception e) {
            LOGGER.error("CSV loading error at line : " + line_index);
        }
        return list;
    }

    private void copyBackgroundImages(int file_no, Long hall_id, String... languages) {
        for (String lang: languages) {
            try {
                String sourcePath = String.format(PATH+HALL_IMAGE,file_no,lang);
                sourcePath = getClass().getResource(sourcePath).getPath();
                sourcePath = URLDecoder.decode(sourcePath,"UTF-8");
                File source = new File(sourcePath);
                File dest = FileUtil.file(String.format(HALL_IMAGE,hall_id,lang));
                FileUtil.copy(source, dest);
            } catch (IOException e) {
                LOGGER.error("failed image copy", e);
            }
        }
    }

}
