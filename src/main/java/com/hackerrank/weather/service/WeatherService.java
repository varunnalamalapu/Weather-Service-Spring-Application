package com.hackerrank.weather.service;

import com.hackerrank.weather.exception.BadRequestException;
import com.hackerrank.weather.exception.WeatherNotFoundException;
import com.hackerrank.weather.model.Temperature;
import com.hackerrank.weather.model.Weather;
import com.hackerrank.weather.model.WeatherInfo;
import com.hackerrank.weather.model.WeatherResponses;
import com.hackerrank.weather.repository.LocationEntity;
import com.hackerrank.weather.repository.LocationRepository;
import com.hackerrank.weather.repository.WeatherEntity;
import com.hackerrank.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final LocationRepository locationRepository;
    private final WeatherRepository weatherRepository;
    private final Transformer transformer;

    @Transactional
    public void deleteAll() {
        locationRepository.deleteAll();
        weatherRepository.deleteAll();
    }

    @Transactional
    public void deleteByDateAndLocation(Date startDate, Date endDate, Float latitude, Float longitude) {
        List<WeatherEntity> weatherEntityList = weatherRepository.findAllByDateRecordedBetween(startDate, endDate);
        List<LocationEntity> locationEntityList = locationRepository.findAllByLatitudeAndLongitude(latitude, longitude);

        locationEntityList.stream()
                .filter(locationEntity -> weatherEntityList.stream().anyMatch(weatherEntity -> weatherEntity.getWeatherId().equals(locationEntity.getWeatherId())))
                .collect(toList())
                .forEach(locationEntity -> {
                    locationRepository.deleteByWeatherId(locationEntity.getWeatherId());
                    weatherRepository.deleteByWeatherId(locationEntity.getWeatherId());
                });
    }

    @Transactional
    public void addWeather(Weather weather) {
        if (ofNullable(locationRepository.findByWeatherId(weather.getId())).isPresent()) {
            throw new BadRequestException(BAD_REQUEST);
        }

        of(transformer.buildWeatherEntity(weather))
                .filter(weatherEntities -> weatherEntities.size() > 0)
                .ifPresent(weatherRepository::save);
        of(transformer.buildLocationEntity(weather.getLocation(), weather.getId()))
                .ifPresent(locationRepository::save);
    }

    public List<WeatherResponses> getAllWeatherInfo() {
        return transformer.buildWeatherList(locationRepository.findAll());
    }

    public List<WeatherResponses> getWeatherInfoByLocation(Float latitude, Float longitude) {
        List<LocationEntity> locationEntities = locationRepository.findAllByLatitudeAndLongitude(latitude, longitude);
        if (locationEntities.size() == 0) {
            throw new WeatherNotFoundException(NOT_FOUND);
        }

        return transformer.buildWeatherList(locationEntities);
    }

    public List<WeatherInfo> getWeatherInfoByDate(Date startDate, Date endDate) {
        List<WeatherInfo> weatherInformation = new ArrayList<>();
        Map<String, Temperature> cityTemperatures = this.getTemperatureDataByCity(startDate, endDate);

        cityTemperatures.keySet()
                .forEach(city -> {
                    LocationEntity locationByCity = locationRepository.findFirstByCityName(city);
                    Temperature tempByCity = cityTemperatures.get(city);
                    WeatherInfo weatherInfo = WeatherInfo.builder()
                            .city(locationByCity.getCityName())
                            .state(locationByCity.getStateName())
                            .lat(locationByCity.getLatitude())
                            .lon(locationByCity.getLongitude())
                            .build();
                    if (null != tempByCity && null != tempByCity.getMaxTemp() && null != tempByCity.getMinTemp()) {
                        weatherInfo.setHighest(tempByCity.getMaxTemp());
                        weatherInfo.setLowest(tempByCity.getMinTemp());
                    } else {
                        weatherInfo.setMessage("There is no weather data in the given date range");
                    }
                    weatherInformation.add(weatherInfo);
                });

        weatherInformation.sort(comparing(WeatherInfo::getCity)
                .thenComparing(WeatherInfo::getState));
        return weatherInformation;
    }

    private Map<String, Temperature> getTemperatureDataByCity(Date startDate, Date endDate) {
        Map<String, Temperature> cityTemperatures = new HashMap<>();

        locationRepository.findAll()
                .forEach(location -> {
                    List<Float> temperaturesByWeatherId = of(weatherRepository.findByWeatherIdAndDateRecordedIsBetween(location.getWeatherId(), startDate, endDate))
                            .filter(weatherEntities -> weatherEntities.size() > 0)
                            .map(weatherEntities -> weatherEntities
                                    .stream()
                                    .map(WeatherEntity::getTemperature)
                                    .sorted()
                                    .collect(toList()))
                            .orElseGet(ArrayList::new);

                    if (temperaturesByWeatherId.size() == 0 && !cityTemperatures.containsKey(location.getCityName())) { //No Temperature with no Pre-existing min and max temp
                        cityTemperatures.put(location.getCityName(), Temperature.builder().maxTemp(null).maxTemp(null).build());
                    } else if (temperaturesByWeatherId.size() > 0 && !cityTemperatures.containsKey(location.getCityName())) { //Temperature Data present with no Pre-existing min and max temp
                        cityTemperatures.put(location.getCityName(), Temperature.builder()
                                .minTemp(temperaturesByWeatherId.get(0))
                                .maxTemp(temperaturesByWeatherId.get(temperaturesByWeatherId.size() - 1))
                                .build());
                    } else if (temperaturesByWeatherId.size() > 0 && cityTemperatures.containsKey(location.getCityName())) { //Temperature Data with existing min and Max temp
                        Temperature maxAndMinTemp = cityTemperatures.get(location.getCityName());

                        Float lowestTemp = temperaturesByWeatherId.get(0);
                        Float highestTemp = temperaturesByWeatherId.get(temperaturesByWeatherId.size() - 1);

                        if (null == maxAndMinTemp.getMinTemp()) {
                            maxAndMinTemp.setMinTemp(lowestTemp);
                        } else {
                            maxAndMinTemp.setMinTemp(lowestTemp < maxAndMinTemp.getMinTemp() ?
                                    lowestTemp : maxAndMinTemp.getMinTemp());
                        }
                        if (null == maxAndMinTemp.getMaxTemp()) {
                            maxAndMinTemp.setMaxTemp(highestTemp);
                        } else {
                            maxAndMinTemp.setMaxTemp(highestTemp > maxAndMinTemp.getMaxTemp() ?
                                    highestTemp : maxAndMinTemp.getMaxTemp());
                        }
                        cityTemperatures.put(location.getCityName(), maxAndMinTemp);
                    }
                });
        return cityTemperatures;
    }
}
