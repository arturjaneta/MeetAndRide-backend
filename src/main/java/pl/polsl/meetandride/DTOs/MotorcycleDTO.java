package pl.polsl.meetandride.DTOs;

import lombok.Data;

@Data
public class MotorcycleDTO {

    private Long id;

    private String brandName;

    private String modelName;

    private int year;

    private int power;

    private int capacity;

    private String registrationNumber;
}
