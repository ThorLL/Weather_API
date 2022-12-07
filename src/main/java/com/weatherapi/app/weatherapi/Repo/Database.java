package com.weatherapi.app.weatherapi.Repo;

import com.weatherapi.app.weatherapi.Exeptions.ModelExceptions.ForecastDoesNotExistException;
import com.weatherapi.app.weatherapi.Exeptions.HttpErrorException;
import com.weatherapi.app.weatherapi.Models.Forecast;
import com.weatherapi.app.weatherapi.Models.Location;
import com.weatherapi.app.weatherapi.OpenWeatherClient.OpenWeatherApiRequester;
import com.weatherapi.app.weatherapi.Units.Degrees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Custom in-memory database for storage of locations
 * Easily replaceable with separate a relational database
 */
public class Database{
    private ArrayList<Location> locations = new ArrayList<>();
    private final String apiKey;

    public Database(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Given a location's id pulls forecast data from the Open Weather map api and inserts it into the database
     * @throws HttpErrorException Forwarding of OpenWeatherApiRequester.getFiveDayForecast's HttpErrorException
     */
    private void pullLocation(long locationId) throws HttpErrorException {
        Location newLocation = OpenWeatherApiRequester.getFiveDayForecast(locationId,apiKey);
        var i = Collections.binarySearch(locations,new Location(locationId));
        if(i < 0){
            locations.add(newLocation);
            Collections.sort(locations);
        }else{
            locations.get(i).getForecasts().putAll(newLocation.getForecasts());
        }
    }

    /**
     * Returns the location associated with the given id
     * If the location can not be found it will execute the order to pull new data from the open weather map api
     * @return location with the given id
     * @throws HttpErrorException Forwarding of OpenWeatherApiRequester.getFiveDayForecast's HttpErrorException
     */
    public Location getLocation(long locationId) throws HttpErrorException {
        var i = Collections.binarySearch(locations,new Location(locationId));
        if(i < 0) {
            pullLocation(locationId);
            i = Collections.binarySearch(locations,new Location(locationId));
        }
        return locations.get(i);
    }

    /**
     * Returns the location associated with the given id
     * If the forecast can not be found it will execute the order to pull new data from the open weather map api
     * such the forecast still not be able an internal server error is assumed and an HttpErrorException is thrown
     * @return location of the given id
     * @throws HttpErrorException
     *      - Forwarding of OpenWeatherApiRequester.getFiveDayForecast's HttpErrorException
     *      - Unable to pull forecast -> internal server error code 500
     */
    public Forecast getForecast(long locationId, LocalDate date) throws HttpErrorException{
        try {
            return getLocation(locationId).getForecastForDate(date);
        } catch (ForecastDoesNotExistException e) {
            pullLocation(locationId);
            try {
                return getLocation(locationId).getForecastForDate(date);
            } catch (ForecastDoesNotExistException ex) {
                throw new HttpErrorException("Unable to pull forecast for the" + date.toString(),500);
            }
        }
    }

    /**
     * Given a location and a date returns a boolean based on whether the locations temperature will be
     * higher than temp
     * @param unit The unit of degrees measurement for temp
     * @param temp The temperature that the given location must be greater than
     * @return boolean determining if the temperature will be greater
     * @throws HttpErrorException Forwarding of getForecast's HttpErrorException
     */
    public boolean ForecastTemperatureIsHigherThan(long locationId, LocalDate date, Degrees unit, int temp) throws HttpErrorException {
        var forecast = getForecast(locationId, date);
        var forecastTemp = forecast.temp();
        if(unit == Degrees.fahrenheit)
            forecastTemp = forecastTemp * 1.8 + 32;
        return temp < forecastTemp;
    }

}