package ru.practicum.explore.apierrors;

import java.time.LocalDateTime;

public class ForbiddenApiError extends ApiError {

    public static ApiError getForbidden(String object) {
        ForbiddenApiError error = new ForbiddenApiError();
        error.setStatus("ERROR_RESPONSE");
        error.setReason("Such params are invalid");
        error.setMessage("The " + object + "with these params are not allowed");
        error.setTimestamp(LocalDateTime.now().toString());
        return error;
    }
}