package com.mehdi.ws_projektarbete_1.service;

import com.mehdi.ws_projektarbete_1.model.Weather;
import com.mehdi.ws_projektarbete_1.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
        this.restTemplate = new RestTemplate();
    }

    public Weather fetchCurrentWeather(String city) {
        try {
            String url = String.format("%s/weather?q=%s&appid=%s", apiUrl, city, apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                WeatherResponse weatherResponse = response.getBody();
                Weather weather = new Weather();
                weather.setCityName(city);
                weather.setTemperature(weatherResponse.getMain().getTemp());
                weather.setHumidity(weatherResponse.getMain().getHumidity());
                weather.setWeatherDescription(weatherResponse.getWeather().get(0).getDescription());
                weather.setTimestamp(LocalDateTime.now());
                return weather;
            } else {
                throw new RuntimeException("Failed to fetch weather data");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching weather data: " + e.getMessage());
        }
    }

    public Weather saveWeather(Weather weather) {
        return weatherRepository.save(weather);
    }

    public Weather updateWeather(Long id, Weather updatedWeather) {
        return weatherRepository.findById(id)
                .map(existingWeather -> {
                    existingWeather.setTemperature(updatedWeather.getTemperature());
                    existingWeather.setHumidity(updatedWeather.getHumidity());
                    existingWeather.setWeatherDescription(updatedWeather.getWeatherDescription());
                    existingWeather.setTimestamp(updatedWeather.getTimestamp());
                    return weatherRepository.save(existingWeather);
                })
                .orElseThrow(() -> new RuntimeException("Weather data not found with id " + id));
    }

    public void deleteWeather(Long id) {
        if (weatherRepository.existsById(id)) {
            weatherRepository.deleteById(id);
        } else {
            throw new RuntimeException("Weather data not found with id " + id);
        }
    }

    public Optional<Weather> getHistoricalWeather(String city, String date) {
        try {
            return weatherRepository.findAll().stream()
                    .filter(weather -> weather.getCityName().equals(city) && weather.getTimestamp().toLocalDate().toString().equals(date))
                    .findFirst();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching historical weather data: " + e.getMessage());
        }
    }

    // Inner class for handling OpenWeatherMap API response
    private static class WeatherResponse {
        private Main main;
        private java.util.List<WeatherDetails> weather;

        // Getters and Setters

        public Main getMain() {
            return main;
        }

        public void setMain(Main main) {
            this.main = main;
        }

        public java.util.List<WeatherDetails> getWeather() {
            return weather;
        }

        public void setWeather(java.util.List<WeatherDetails> weather) {
            this.weather = weather;
        }
    }

    private static class Main {
        private double temp;
        private int humidity;

        // Getters and Setters

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }

    private static class WeatherDetails {
        private String description;

        // Getters and Setters

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
