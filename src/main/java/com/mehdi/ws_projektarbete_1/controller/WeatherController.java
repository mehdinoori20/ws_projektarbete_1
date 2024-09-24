package com.mehdi.ws_projektarbete_1.controller;


import com.mehdi.ws_projektarbete_1.model.Weather;
import com.mehdi.ws_projektarbete_1.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }




}

