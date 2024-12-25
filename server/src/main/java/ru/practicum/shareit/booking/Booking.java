package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "time_start", nullable = false)
    private LocalDateTime start;
    @Column(name = "time_end", nullable = false)
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker")
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 25)
    private BookingStatus status = BookingStatus.WAITING;
}
