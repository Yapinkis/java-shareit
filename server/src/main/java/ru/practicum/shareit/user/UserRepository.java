package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT b.booker FROM Booking b WHERE b.item.id = ?1 AND b.status = 'APPROVED'")
    User findBookerByItemId(Long itemId);

    /**
     Валидация User
     **/

    boolean existsByEmail(String email);


}