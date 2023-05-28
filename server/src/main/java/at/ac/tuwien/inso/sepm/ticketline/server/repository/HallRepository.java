package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {

    /**
     * Finds a hall entry from the database by perfomance id
     * @param id id of a performance
     * @return Hall entry or null
     */
    @Query(value = "select * from Hall h where h.id in (select p.hall_id from Performance p where p.id = :id)", nativeQuery = true)
    Hall findByPerformanceId(@Param("id")Long id);

}
