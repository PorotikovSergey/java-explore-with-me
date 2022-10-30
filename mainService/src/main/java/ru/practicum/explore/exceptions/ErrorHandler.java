package ru.practicum.explore.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.apierrors.ApiError;
import ru.practicum.explore.apierrors.ForbiddenApiError;
import ru.practicum.explore.apierrors.NotFoundApiError;
import ru.practicum.explore.apierrors.ServerApiError;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)  //400
    public ApiError handleValidationException(final ValidationException e) {
        return new ForbiddenApiError();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)  //404
    public ApiError handleNotFoundException(final NotFoundException e) {
        return new NotFoundApiError();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  //500
    public ApiError handleThrowable(final ServerException e) {
        return new ServerApiError();
    }

}
