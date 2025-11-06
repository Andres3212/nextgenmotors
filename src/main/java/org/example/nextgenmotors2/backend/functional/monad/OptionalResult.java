package org.example.nextgenmotors2.backend.functional.monad;

import org.example.nextgenmotors2.backend.model.entity.Reservation;

import java.util.Optional;
import java.util.function.Function;

public class OptionalResult<T> extends Reservation {
    private final Optional<T> value;
    private final String error;

    private OptionalResult(Optional<T> value, String error) {
        this.value = value;
        this.error = error;
    }

    public static <T> OptionalResult<T> success(T value) {
        return new OptionalResult<>(Optional.ofNullable(value), null);
    }

    public static <T> OptionalResult<T> failure(String error) {
        return new OptionalResult<>(Optional.empty(), error);
    }

    public <R> OptionalResult<R> map(Function<T, R> mapper) {
        return value.map(v -> OptionalResult.success(mapper.apply(v)))
                .orElse(OptionalResult.failure(error));
    }

    public <R> OptionalResult<R> flatMap(Function<T, OptionalResult<R>> mapper) {
        return value.map(mapper)
                .orElse(OptionalResult.failure(error));
    }

    public T getOrElse(T defaultValue) {
        return value.orElse(defaultValue);
    }

    public boolean isSuccess() {
        return value.isPresent();
    }

    public Optional<T> getValue() {
        return value;
    }

    public String getError() {
        return error;
    }
}