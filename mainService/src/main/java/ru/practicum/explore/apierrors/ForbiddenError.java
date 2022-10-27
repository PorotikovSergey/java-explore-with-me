package ru.practicum.explore.apierrors;

import java.time.LocalDateTime;

public class ForbiddenError extends ApiError {

    public static ApiError getForbidden(String object) {
        ForbiddenError error = new ForbiddenError();
        error.setStatus("ERROR_RESPONSE");
        error.setReason("Such params are invalid");
        error.setMessage("The " + object + "with these params are not allowed");
        error.setTimestamp(LocalDateTime.now().toString());
        return error;
    }
}
