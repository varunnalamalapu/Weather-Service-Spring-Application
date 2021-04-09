package com.hackerrank.weather.controller;

import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.model.WeatherInfo;
import com.hackerrank.weather.model.WeatherResponses;
import com.hackerrank.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class WeatherApiRestController {

    private final WeatherService weatherService;

    @DeleteMapping(value = "/erase")
    public ResponseEntity deleteAll() {
        weatherService.deleteAll();
        return ResponseEntity.status(OK).build();
    }

    @DeleteMapping(value = "/erase", params = {"start", "end", "lat", "lon"})
    public ResponseEntity deleteByDate(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                       @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                       @RequestParam(name = "lat") Float latitude,
                                       @RequestParam(name = "lon") Float longitude) {
        weatherService.deleteByDateAndLocation(startDate, endDate, latitude, longitude);
        return ResponseEntity.status(OK).build();
    }

    @PostMapping(value = "/weather")
    public ResponseEntity addWeather(@RequestBody Weather weather) {
        weatherService.addWeather(weather);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping(value = "/weather")
    public ResponseEntity<List<WeatherResponses>> getAllWeatherInformation() {
        return ResponseEntity.ok(weatherService.getAllWeatherInfo());
    }

    @GetMapping(value = "/weather", params = {"lat", "lon"})
    public ResponseEntity<List<WeatherResponses>> getWeatherInfoByLocation(@RequestParam(name = "lat") Float latitude,
                                                                           @RequestParam(name = "lon") Float longitude) {
        return ResponseEntity.ok(weatherService.getWeatherInfoByLocation(latitude, longitude));
    }

    @GetMapping(value = "/weather/temperature")
    public ResponseEntity<List<WeatherInfo>> getWeatherInfoByDate(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                                  @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(weatherService.getWeatherInfoByDate(startDate, endDate));
    }
}
