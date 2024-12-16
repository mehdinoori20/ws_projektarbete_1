package com.mehdi.ws_projektarbete_1;

import com.mehdi.ws_projektarbete_1.controller.WeatherController;
import com.mehdi.ws_projektarbete_1.exception.CityNotFoundException;
import com.mehdi.ws_projektarbete_1.model.Weather;
import com.mehdi.ws_projektarbete_1.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherControllerTest {

    private final WeatherService weatherService = mock(WeatherService.class);
    private final WeatherController weatherController = new WeatherController(weatherService);

    @Test
    void testShowWeatherPage_ValidCity() {
        String city = "Stockholm";
        Weather weather = new Weather();
        weather.setCityName(city);
        weather.setTemperature(20.5);

        Model model = mock(Model.class);
        when(weatherService.fetchCurrentWeather(city)).thenReturn(weather);

        String viewName = weatherController.showWeatherPage(city, model);

        assertEquals("weather", viewName, "Should return the weather view name");
        verify(model).addAttribute("weather", weather);
    }

    @Test
    void testShowWeatherPage_InvalidCity() {
        String city = " ";
        Model model = mock(Model.class);

        String viewName = weatherController.showWeatherPage(city, model);

        assertEquals("weather", viewName, "Should return the weather view name even for invalid city");
        verify(model).addAttribute(eq("error"), anyString());
    }

    @Test
    void testShowWeatherPage_CityNotFound() {
        String city = "UnknownCity";
        Model model = mock(Model.class);
        when(weatherService.fetchCurrentWeather(city)).thenThrow(new CityNotFoundException("City not found"));

        String viewName = weatherController.showWeatherPage(city, model);

        assertEquals("weather", viewName, "Should return the weather view name when city is not found");
        verify(model).addAttribute("error", "Staden kunde inte hittas. Kontrollera att du angivit rätt namn.");
    }

    @Test
    void testGetCurrentWeather_ValidCity() {
        String city = "Stockholm";
        Weather weather = new Weather();
        weather.setCityName(city);

        when(weatherService.fetchCurrentWeather(city)).thenReturn(weather);

        ResponseEntity<Weather> response = weatherController.getCurrentWeather(city);

        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Should return HTTP 200 OK");
        assertEquals(weather, response.getBody(), "Response body should match the weather object");
    }

    @Test
    void testGetCurrentWeather_InvalidCity() {
        String city = "";
        ResponseEntity<Weather> response = weatherController.getCurrentWeather(city);

        assertNotNull(response, "Response should not be null");
        assertEquals(400, response.getStatusCodeValue(), "Should return HTTP 400 Bad Request for invalid city");
    }

    @Test
    void testGetCurrentWeather_CityNotFound() {
        String city = "UnknownCity";
        when(weatherService.fetchCurrentWeather(city)).thenThrow(new CityNotFoundException("City not found"));

        ResponseEntity<Weather> response = weatherController.getCurrentWeather(city);

        assertNotNull(response, "Response should not be null");
        assertEquals(404, response.getStatusCodeValue(), "Should return HTTP 404 Not Found for city not found");
    }

    @Test
    void testCreateWeather_Success() {
        Weather weather = new Weather();
        weather.setCityName("Stockholm");
        when(weatherService.saveWeather(weather)).thenReturn(weather);

        ResponseEntity<Weather> response = weatherController.createWeather(weather);

        assertNotNull(response, "Response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "Should return HTTP 200 OK for successful creation");
        assertEquals(weather, response.getBody(), "Response body should match the saved weather object");
    }

    @Test
    void testCreateWeather_Failure() {
        Weather weather = new Weather();
        when(weatherService.saveWeather(weather)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Weather> response = weatherController.createWeather(weather);

        assertNotNull(response, "Response should not be null");
        assertEquals(400, response.getStatusCodeValue(), "Should return HTTP 400 Bad Request for failure");
    }

    @Test
    void testDeleteWeather_Success() {
        Long id = 1L;

        ResponseEntity<Void> response = weatherController.deleteWeather(id);

        assertNotNull(response, "Response should not be null");
        assertEquals(204, response.getStatusCodeValue(), "Should return HTTP 204 No Content for successful deletion");
    }

    @Test
    void testDeleteWeather_NotFound() {
        Long id = 1L;
        doThrow(new RuntimeException("Not found"))
                .when(weatherService).deleteWeather(id);

        ResponseEntity<Void> response = weatherController.deleteWeather(id);

        assertNotNull(response, "Response should not be null");
        assertEquals(404, response.getStatusCodeValue(), "Should return HTTP 404 Not Found for missing weather data");
    }
}