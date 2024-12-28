package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest,Long> {
    @Query("SELECT ir FROM ItemRequest ir WHERE ir.user.id = ?1")
    List<ItemRequest> findRequestsByUser(Long id);

    List<ItemRequest> findAllByUserIdNot(Long id, PageRequest pageRequest);

    Optional<ItemRequest> findItemRequestById(Long id);

}
