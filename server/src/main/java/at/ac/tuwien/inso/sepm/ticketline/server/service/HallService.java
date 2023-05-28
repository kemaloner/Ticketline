package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;

public interface HallService {

    /**
     * Finds a hall entry from the database by perfomance id
     * @param id id of a performance
     * @return Hall entry or null
     */
    Hall findByPerformanceId(Long id);
}
