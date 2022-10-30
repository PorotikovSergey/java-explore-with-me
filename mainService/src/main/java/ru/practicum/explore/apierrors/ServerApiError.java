package ru.practicum.explore.apierrors;

import java.time.LocalDateTime;

public class ServerApiError extends ApiError {

    public static ApiError getServerError(String message) {
        ApiError apiError = new ApiError();
        apiError.setStatus("INTERNAL_SERVER_ERROR");
        apiError.setReason("Error occurred");
        apiError.setMessage(message);
        apiError.setTimestamp(LocalDateTime.now().toString());
        return apiError;
    }
}
