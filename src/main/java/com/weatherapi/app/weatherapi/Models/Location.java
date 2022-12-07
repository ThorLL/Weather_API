package com.weatherapi.app.weatherapi.Models;

import com.weatherapi.app.weatherapi.Exeptions.ModelExceptions.ForecastDoesNotExistException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * City location
 * Contains the id of the city and a hashmap of forecasts where keys are LocalDateTimes and values are Forecasts
 */
public class Location implements Comparable<Location>{
    private final long id;
    private HashMap<LocalDateTime, Forecast> forecasts;
    public Location(long id) {
        this.id = id;
        forecasts = new HashMap<>();
    }
    public long getId() {
        return id;
    }

    public HashMap<LocalDateTime, Forecast> getForecasts() {
        return forecasts;
    }

    /**
     * @param date The date to be searched
     * @return A list of forecast for the specified date
     */
    public List<Forecast> getForecasts(LocalDate date) {
        ArrayList<Forecast> forecastsFiltered = new ArrayList<>();
        for(var forecastDate : forecasts.keySet())
            if (forecastDate.toLocalDate().equals(date))
                forecastsFiltered.add(forecasts.get(forecastDate));
        return forecastsFiltered;
    }

    /**
     * @param date The date to be search
     * @return A forecast with the average, minimum and maximum temperatures of that day
     * @throws ForecastDoesNotExistException Exception is thrown when there exists no forecasts for the specified date
     */
    public Forecast getForecastForDate(LocalDate date) throws ForecastDoesNotExistException {
        List<Forecast> forecastsFiltered = getForecasts(date);

        if(forecastsFiltered.size() == 0)
            throw new ForecastDoesNotExistException("No forecast for the" + date.toString());
        double temp = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (var forecast : forecastsFiltered){
            temp += forecast.temp();
            min = Math.min(forecast.temp_min(), min);
            max = Math.max(forecast.temp_max(), max);
        }
        return new Forecast(temp/forecastsFiltered.size(),min,max);
    }
    public void addForecast(LocalDateTime date, Forecast forecast) {
        forecasts.put(date,forecast);
    }

    /**
     * Compares two locations based on their id, method of the implemented Comparable<Location> interface
     * @param l the object to be compared.
     * @return Returns an integer based on which of the two compare locations has the greater id (negative if location l is greater), if equal returns 0
     */
    @Override
    public int compareTo(Location l) {
        return (int) (this.id - l.getId());
    }
}
