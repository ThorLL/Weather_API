package com.weatherapi.app.weatherapi.OpenWeatherClient.ResponseStructures;
/**
 * Class representing the json datastructures given from OpenWeatherMap
 * Used to extract information from the OpenWeatherMap api json response
 */
public class RawForecast {
    private Temps main;
    private String dt_txt;

    public Temps getMain() {
        return main;
    }

    public void setMain(Temps main) {
        this.main = main;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
