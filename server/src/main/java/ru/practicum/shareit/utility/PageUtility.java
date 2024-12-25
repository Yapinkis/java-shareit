package ru.practicum.shareit.utility;

import jakarta.validation.ValidationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageUtility {
    public static PageRequest form(int from, int size, Sort.Direction direction, String properties) {
        if (from < 0) throw new ValidationException("Пагинация недоступна с параметром from < 0");
        if (size <= 0) throw new ValidationException("Пагинация недоступна с параметром size > 0");
        Sort sort = Sort.by(direction, properties);
        return PageRequest.of(from / size, size, sort);
    }

}
