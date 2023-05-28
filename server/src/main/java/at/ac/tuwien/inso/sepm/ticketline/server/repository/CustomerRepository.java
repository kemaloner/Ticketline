package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find a single customer entry by id.
     *
     * @param id the is of the customer entry
     * @return Optional containing the customer entry
     */
    Optional<Customer> findOneById(Long id);

    /**
     *
     * @param keyword substring of firstname, surname, email, address, birthday or phone number the wanted costumers
     * @return a list of customer
     */
    @Query(value = "SELECT * FROM CUSTOMER WHERE UPPER(CONCAT(FIRSTNAME,' ',SURNAME,' ',EMAIL,' ',ADDRESS,' ',BIRTHDAY,' ',PHONE_NUMBER)) LIKE CONCAT('%', UPPER(:keyword), '%')", nativeQuery = true)
    Page<Customer> findByKeyword(@Param("keyword") String keyword, Pageable request);

}
