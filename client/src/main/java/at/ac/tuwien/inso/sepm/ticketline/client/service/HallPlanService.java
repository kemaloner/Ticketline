package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SeatSelectionException;
import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.performance.PerformanceDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;

import java.io.File;
import java.util.List;

public interface HallPlanService {

    /**
     * Fetches a hallDTO in which the performance with the given id is played
     * @param id id of the performance
     * @return Hall in which the performance is played
     * @throws DataAccessException in case something went wrong
     */
    HallDTO findHallByPerformanceId(Long id) throws DataAccessException;

    /**
     * Fetches the ticket seat objects for the performance with the performance id
     * @param performance_id id of the performance
     * @return a list of TicketSeatDTO objects
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleTicketSeatDTO> findTicketSeatByPerformanceId(Long performance_id) throws DataAccessException;

    /**
     * create a temporary booking
     * @param performanceDTO performance
     * @param seatDTOS selected seats
     * @throws SeatSelectionException if at least one seat, which was selected by user was already taken
     * @throws DataAccessException in case something went wrong
     */
    void save(PerformanceDTO performanceDTO, List<SimpleTicketSeatDTO> seatDTOS) throws DataAccessException, SeatSelectionException;

    /**
     * update a booking
     * @param ticketDTO booking
     * @param seatDTOS selected seats
     * @throws SeatSelectionException if at least one seat, which was selected by user was already taken
     * @throws DataAccessException in case something went wrong
     */
    void save(TicketDTO ticketDTO, List<SimpleTicketSeatDTO> seatDTOS) throws SeatSelectionException, DataAccessException;

    /**
     * Fetches a new unique reservation number from the database
     * @return String representing reservation number
     * @throws DataAccessException in case something went wrong
     */
    String requestNewReservationNumber() throws DataAccessException;

    /**
     * Calculates the subtotal of a ticket
     * @param performanceDTO Performance, of which we need the baseprice
     * @param seatDTOS selected seats, of which we will get their multiplier
     * @return subtotal price of the ticket
     */
    Double calculateTicketSubTotal(PerformanceDTO performanceDTO, List<SeatDTO> seatDTOS);

    /**
     * load from server BackgroundImage data of Hall and save cache to client
     * @param hall_id hall id
     * @param language language code
     * @return cached file in client
     * @throws DataAccessException in case something went wrong
     */
    File getBackgroundImage(Long hall_id, String language) throws DataAccessException;

    TicketDTO getSavedTicket();

    void setSavedTicket(TicketDTO ticket);
}
