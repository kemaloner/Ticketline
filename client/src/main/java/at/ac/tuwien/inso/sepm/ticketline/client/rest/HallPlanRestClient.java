package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.hall.HallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticketseat.SimpleTicketSeatDTO;

import java.util.List;

public interface HallPlanRestClient {

    HallDTO findHallByPerformanceId(Long id) throws DataAccessException;

    List<SimpleTicketSeatDTO> findTicketSeatsByPerformanceId(Long performance_id) throws DataAccessException;

    byte[] getBackgroundImage(Long hall_id, String lang) throws DataAccessException;
}
