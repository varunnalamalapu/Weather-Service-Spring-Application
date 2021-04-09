package com.hackerrank.weather.service;

import com.hackerrank.weather.model.Location;
import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.model.WeatherResponses;
import com.hackerrank.weather.repository.LocationEntity;
import com.hackerrank.weather.repository.WeatherEntity;
import com.hackerrank.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class Transformer {

    private final WeatherRepository weatherRepository;

    public LocationEntity buildLocationEntity(Location location, Long weatherId) {
        return LocationEntity.builder()
                .cityName(location.getCity())
                .latitude(location.getLat())
                .longitude(location.getLon())
                .stateName(location.getState())
                .weatherId(weatherId)
                .build();
    }

    public List<WeatherEntity> buildWeatherEntity(Weather weather) {

        List<Float> temperatures = Arrays.asList(weather.getTemperature());
        return temperatures.stream()
                .map(temperature -> WeatherEntity.builder()
                        .dateRecorded(weather.getDate())
                        .weatherId(weather.getId())
                        .temperature(temperature)
                        .build())
                .collect(toList());
    }

    public List<WeatherResponses> buildWeatherList(List<LocationEntity> locationEntities) {
        List<WeatherResponses> weatherList = new ArrayList<>();
        locationEntities
                .forEach(location -> {
                    List<WeatherEntity> weatherEntities = weatherRepository.findByWeatherId(location.getWeatherId())
                            .filter(weatherEntityList -> weatherEntityList.size() > 0)
                            .get();
                    weatherList.add(WeatherResponses.builder()
                            .id(location.getWeatherId())
                            .location(Location.builder()
                                    .lat(location.getLatitude())
                                    .lon(location.getLongitude())
                                    .city(location.getCityName())
                                    .state(location.getStateName())
                                    .build())
                            .temperature(weatherEntities.stream()
                                    .map(WeatherEntity::getTemperature)
                                    .collect(toList()))
                            .date(weatherEntities.stream()
                                    .findAny()
                                    .map(WeatherEntity::getDateRecorded)
                                    .map(date -> new SimpleDateFormat("yyyy-MM-dd").format(date))
                                    .orElse(null))
                            .build());
                });
        return weatherList;
    }

}
