package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {
    private final String error;
    //private final String description;

    public ErrorResponse(String error) {
        this.error = error;
        //this.description = description;
    }
}
