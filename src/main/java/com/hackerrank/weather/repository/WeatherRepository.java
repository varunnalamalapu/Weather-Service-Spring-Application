package com.hackerrank.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Integer> {

    List<WeatherEntity> findAllByDateRecordedBetween(Date startDate, Date endDate);

    void deleteByWeatherId(Long weatherId);

    Optional<List<WeatherEntity>> findByWeatherId(Long weatherId);

    List<WeatherEntity> findByWeatherIdAndDateRecordedIsBetween(Long weatherId, Date startDate, Date endDate);
}
