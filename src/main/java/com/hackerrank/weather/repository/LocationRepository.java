package com.hackerrank.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {

    List<LocationEntity> findAllByLatitudeAndLongitude(Float latitude, Float longitude);

    void deleteByWeatherId(Long weatherId);

    LocationEntity findByWeatherId(Long weatherId);

    LocationEntity findFirstByCityName(String cityName);
}
