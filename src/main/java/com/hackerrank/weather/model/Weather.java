package com.hackerrank.weather.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hackerrank.weather.util.CustomDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    private Long id;

    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date date;

    private Location location;

    private Float[] temperature;

}
