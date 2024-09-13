package com.mehdi.ws_projektarbete_1.repository;

import com.mehdi.ws_projektarbete_1.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
