package ru.prakticum.shareit.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exception.ManagerExceptionHandler;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ManagerExceptionHandlerTest {
    @InjectMocks
    private ManagerExceptionHandler managerExceptionHandler;

    @Test
    void handleValidationError_shouldReturnBadRequestWithErrorMessage() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("objectName",
                "name",
                "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, String>> response =
                managerExceptionHandler.handleValidationError(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Map<String, String> body = response.getBody();
        assertNotNull(body, "Body не должно быть null");
        assertTrue(body.containsKey("error"),
                "В теле ответа должен быть ключ 'error'");

        String errorMessage = body.get("error");
        assertEquals("name: must not be blank", errorMessage);
    }
}
