package ru.practicum.explore.apierrors;

import java.time.LocalDateTime;

public class NotFoundApiError extends ApiError {
    public static ApiError getNotFound(String object) {
        NotFoundApiError error = new NotFoundApiError();
        error.setStatus("NOT_FOUND");
        error.setReason("The required objects were not found.");
        error.setMessage("The " + object + "with these params not found");
        error.setTimestamp(LocalDateTime.now().toString());
        return error;
    }
}
