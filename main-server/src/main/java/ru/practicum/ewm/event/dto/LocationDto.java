package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDto {
    private Double lat; // Широта
    private Double lon; // Долгота
}
