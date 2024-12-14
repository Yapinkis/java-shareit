package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByOwnerId(Long id);

    @Query("SELECT i FROM Item i WHERE UPPER(i.name) = UPPER(?1) AND i.available = true")
    List<Item> search(String name);

    @Query("SELECT i FROM Item i WHERE i.owner.id = ?1")
    List<Item> getAllFromUser(Long id);

}
