package ru.practicum.explore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)  //400
    public ApiError handleValidationException(final ValidationException e) {
        ForbiddenApiError error = new ForbiddenApiError();
        error.setStatus("BAD_REQUEST");
        error.setReason("Невалидный запрос");
        error.setStacktrace(e.getStackTrace());
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now().toString());
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)  //404
    public ApiError handleNotFoundException(final NotFoundException e) {
        NotFoundApiError error = new NotFoundApiError();
        error.setStacktrace(e.getStackTrace());
        error.setStatus("NOT_FOUND");
        error.setReason("Объект не найден");
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now().toString());
        return error;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  //500
    public ApiError handleThrowable(final ServerException e) {
        ServerApiError error = new ServerApiError();
        error.setStacktrace(e.getStackTrace());
        error.setStatus("INTERNAL_SERVER_ERROR");
        error.setReason("Ошибка на сервере");
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now().toString());
        return error;
    }

}
