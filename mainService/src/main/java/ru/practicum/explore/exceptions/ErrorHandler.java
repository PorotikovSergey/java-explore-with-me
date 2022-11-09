package ru.practicum.explore.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)  //400
    public ApiError handleValidationException(final ValidationException e) {
        ForbiddenApiError error = new ForbiddenApiError();
        error.setStatus("BAD_REQUEST");
        error.setReason("Невалидный запрос");
        error.setErrors(e.getStackTrace());
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now().toString());
        log.error("Неправильный запрос. Ошибка 400");
        log.error(e.getMessage());
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)  //400
    public ApiError handleMethodArgumentException(final MethodArgumentNotValidException e) {
        ForbiddenApiError error = new ForbiddenApiError();
        error.setStatus("BAD_REQUEST");
        error.setReason("Невалидные данные для создания объекта");
        error.setErrors(e.getStackTrace());
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now().toString());
        log.error("Невалидные данные. Ошибка 400");
        log.error(e.getMessage());
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)  //404
    public ApiError handleNotFoundException(final NotFoundException e) {
        NotFoundApiError error = new NotFoundApiError();
        error.setErrors(e.getStackTrace());
        error.setStatus("NOT_FOUND");
        error.setReason("Объект не найден");
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now().toString());
        log.error("Не найдено. Ошибка 404");
        log.error(e.getMessage());
        return error;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  //500
    public ApiError handleThrowable(final ServerException e) {
        ServerApiError error = new ServerApiError();
        error.setErrors(e.getStackTrace());
        error.setStatus("INTERNAL_SERVER_ERROR");
        error.setReason("Ошибка на сервере");
        error.setMessage(e.getMessage());
        error.setTimestamp(LocalDateTime.now().toString());
        log.error("Ошибка на сервере. Ошибка 500");
        log.error(e.getMessage());
        return error;
    }

}
