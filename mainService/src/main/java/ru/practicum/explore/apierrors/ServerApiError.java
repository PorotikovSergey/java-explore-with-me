package ru.practicum.explore.apierrors;

import java.time.LocalDateTime;

public class ServerApiError extends ApiError {

    public static ApiError getServerError() {
        ApiError apiError = new ApiError();
        apiError.setStatus("INTERNAL_SERVER_ERROR");
        apiError.setReason("Error occurred");
        apiError.setMessage("could not execute statement; SQL [n/a]; constraint [uq_category_name]; " +
                "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                "could not execute statement");
        apiError.setTimestamp(LocalDateTime.now().toString());
        return apiError;
    }
}
