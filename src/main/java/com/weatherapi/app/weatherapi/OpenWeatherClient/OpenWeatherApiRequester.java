package com.weatherapi.app.weatherapi.OpenWeatherClient;

import com.google.gson.Gson;
import com.weatherapi.app.weatherapi.Exeptions.HttpErrorException;
import com.weatherapi.app.weatherapi.Models.Forecast;
import com.weatherapi.app.weatherapi.Models.Location;
import com.weatherapi.app.weatherapi.OpenWeatherClient.ResponseStructures.FiveDayForecast;
import com.weatherapi.app.weatherapi.OpenWeatherClient.ResponseStructures.Temps;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class used to call the Open Weather Map api to get weather forecasts
 */
public class OpenWeatherApiRequester {
    /**
     * List that, as calls are made, gets filled with location ids that do not correspond to any city
     */
    private static ArrayList<Long> blacklistedLocationIds = new ArrayList<>();

    /**
     * @param apiKey Your Open Weather Map API key also known as appid
     * @return returns a location object containing today and the next five days forecasts at 3 hour intervals.
     * @throws HttpErrorException Thrown at three instances:
     *      1. An id from blacklisted was given, results in HTTP status code 404 NOT FOUND
     *      2. There is a syntax issue with the uri, might be the api key or the api uri has changed results in
     *         HTTP status code 500 INTERNAL SERVER ERROR
     *      3. The api call was success but open weather map's api responded with an error, forwards the given error code
     */
    public static Location getFiveDayForecast(long locationId, String apiKey) throws HttpErrorException {
        if(blacklistedLocationIds.contains(locationId))
            throw new HttpErrorException("city with that id doesn't exist",404);

        var uri = "https://api.openweathermap.org/data/2.5/forecast?units=metric&id=" + locationId + "&appid=" + apiKey;

        // Creates the HTTP get request for the open weather map api
        HttpRequest getRequest;
        try {
            getRequest = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .build();
        } catch (URISyntaxException e) {
            throw new HttpErrorException("Internal Server Error ",500);
        }
        // Sends HTTP request to open weather map api and reads response
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> getResponse;
        try {
            getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }

        // Checks responses status
        switch (getResponse.statusCode()) {
            // Success
            case 200 -> {
                // Convert json to forecasts
                Gson gson = new Gson();
                var fiveDayForecast = gson.fromJson(getResponse.body(), FiveDayForecast.class);
                Location location = new Location(locationId);
                for (var forecast : fiveDayForecast.getList()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(forecast.getDt_txt(), formatter);
                    Temps temps = forecast.getMain();
                    location.addForecast(dateTime, new Forecast(temps.getTemp(), temps.getTemp_min(), temps.getTemp_max()));
                }
                return location;
            }
            // City does not exist - add to blacklist and throw HttpErrorException
            case 404 -> {
                blacklistedLocationIds.add(locationId);
                throw new HttpErrorException("No city with id " + locationId + " exist", 404);
            }
            // Something went wrong - forward HTTP status code
            default -> throw new HttpErrorException("Something went wrong", getResponse.statusCode());
        }
    }
}
