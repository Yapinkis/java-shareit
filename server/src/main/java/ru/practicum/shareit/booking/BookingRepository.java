package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.item.id = ?2 AND b.status = 'APPROVED' AND b.end < CURRENT_TIMESTAMP")
    Optional<Booking> getBookingFromUserAndItem(Long userId, Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.end < CURRENT_TIMESTAMP ORDER BY b.end DESC")
    Booking getLastBooking(Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.item.id = ?1 AND b.start > CURRENT_TIMESTAMP ORDER BY b.start ASC")
    Booking getNextBooking(Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemsIds AND b.end < CURRENT_TIMESTAMP ORDER BY b.end DESC")
    List<Booking> getAllLastBookings(List<Long> itemsIds);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemsIds AND b.start > CURRENT_TIMESTAMP ORDER BY b.start ASC")
    List<Booking> getAllNextBookings(List<Long> itemsIds);

    /**
     * Набор методов для получение списка всех бронирований текущего пользователя
     * @param userId идентификатор пользователя, для которого нужно получить бронирования.
     * @return список бронирований пользователя.
     */
    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1")
    List<Booking> findAllBookingsByUser(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start <= ?2 AND b.end >= ?2")
    List<Booking> findCurrentBookings(Long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.end < ?2")
    List<Booking> findPastBookings(Long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.start > ?2")
    List<Booking> findFutureBookings(Long userId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?1 AND b.status = ?2")
    List<Booking> findBookingsByStatus(Long userId, BookingStatus status);

    /**
     * Набор методов для получения списка бронирований для всех вещей текущего пользователя.
     * @param ownerId идентификатор пользователя, для которого нужно получить список вещей.
     * @return список бронирований пользователя.
     */

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1")
    List<Booking> findAllBookingsForOwner(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.start <= ?2 AND b.end >= ?2")
    List<Booking> findCurrentBookingsForOwner(Long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.end < ?2")
    List<Booking> findPastBookingsForOwner(Long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.start > ?2")
    List<Booking> findFutureBookingsForOwner(Long ownerId, LocalDateTime now);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = ?1 AND b.status = ?2")
    List<Booking> findBookingsForOwnerByStatus(Long ownerId, BookingStatus status);

}
