package com.weatherapi.app.weatherapi.Controller;

import com.google.gson.Gson;
import com.weatherapi.app.weatherapi.Exeptions.HttpErrorException;
import com.weatherapi.app.weatherapi.Models.Forecast;
import com.weatherapi.app.weatherapi.Repo.Database;
import com.weatherapi.app.weatherapi.Units.Degrees;
import com.weatherapi.app.weatherapi.WeatherapiApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController()
public class ApiControllers {
    private final Database database = new Database(WeatherapiApplication.getApiKey());
    private final Gson gson = new Gson();
    /**
     * Give a list of location ids returns all locations ids where the corresponding temperature for the next day is
     * higher than that of temp.
     * @param unit Degree unit type of temperature (Celsius or Fahrenheit) if not give defaults to celsius
     * @param temp The temperature the location but be above, RequestParam
     * @param locations The ids of the locations to search, seperated by commas ','
     * @return An HTTP response with the ids of the locations.
     */
    @GetMapping(value = "/weather/summary")
    public ResponseEntity<String> getSummary(@RequestParam(required = false) Degrees unit, @RequestParam int temp, @RequestParam String locations){
        if(unit == null)
            unit = Degrees.celsius;
        var tomorrow = LocalDate.now().plusDays(1);
        var locationForecasts = new ArrayList<Long>();

        for (var locationId : locations.split(",")) {
            try {
                if(database.ForecastTemperatureIsHigherThan(Long.parseLong(locationId), tomorrow, unit, temp))
                    locationForecasts.add(Long.parseLong(locationId));
            } catch (HttpErrorException e) {
                return new ResponseEntity<>(HttpStatusCode.valueOf(e.getStatusCode()));
            }
        }
        return new ResponseEntity<>(gson.toJson(locationForecasts),HttpStatus.OK);
    }

    /**
     * Given a location id returns the average, minimum and maximum temperatures for the next 5 days in that location
     * @param location The location id of the location to be selected
     * @return An HTTP response with the temperatures in a json format
     */
    @GetMapping(value = "/weather/locations/id")
    public ResponseEntity<String> getForecasts(@RequestParam String location){
        var forecasts = new Forecast[5];
        var date = LocalDate.now();
        for(int i = 0; i < 5; i++){
            date = date.plusDays(1);
            try {
                forecasts[i] = database.getForecast(Long.parseLong(location), date);
            } catch (HttpErrorException e) {
                return new ResponseEntity<>(HttpStatusCode.valueOf(e.getStatusCode()));
            }
        }
        return new ResponseEntity<>(gson.toJson(forecasts),HttpStatus.OK);
    }

}
